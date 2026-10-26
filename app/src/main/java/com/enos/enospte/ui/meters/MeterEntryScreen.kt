package com.enos.enospte.ui.meters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enos.enospte.data.datastore.MeterRoutineConfig
import com.enos.enospte.data.datastore.MeterRoutineStorage
import com.enos.enospte.data.model.Meter
import com.enos.enospte.data.model.Reading
import com.enos.enospte.data.source.MeterCatalog
import com.enos.enospte.ui.meters.components.EmptyMetersCard
import com.enos.enospte.ui.meters.components.MeterEntryHeader
import com.enos.enospte.ui.meters.components.MeterInputSection
import com.enos.enospte.ui.meters.components.MeterViewSelector
import com.enos.enospte.ui.meters.components.RecentReadingsSection
import com.enos.enospte.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun MeterEntryScreen(
    enteredBy: String,
    userId: String,
    isAdmin: Boolean,
    onLogout: () -> Unit,
    onAdminHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    vm: MainViewModel = viewModel()
) {
    val readings by vm.readings.collectAsState()

    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val baseMeters = remember { MeterCatalog.getMeters() }
    val routineStorage = remember { MeterRoutineStorage(context) }

    val routineConfig by routineStorage.observeConfig(userId)
        .collectAsState(initial = MeterRoutineConfig())

    val routines = remember(routineConfig) {
        routineConfig.routines
    }

    val activeRoutine = remember(routineConfig) {
        routineConfig.routines.firstOrNull {
            it.id == routineConfig.activeRoutineId
        }
    }

    val meterById = remember(baseMeters) {
        baseMeters.associateBy { it.id }
    }

    val sites = remember(baseMeters) {
        baseMeters.map { it.site }.distinct().sorted()
    }

    var selectedSite by remember {
        mutableStateOf(sites.firstOrNull().orEmpty())
    }

    val routinesForSelectedSite = remember(
        routines,
        selectedSite,
        meterById
    ) {
        routines.filter { routine ->
            routine.meterIds.any { meterId ->
                meterById[meterId]?.site == selectedSite
            }
        }
    }

    LaunchedEffect(
        selectedSite,
        activeRoutine?.id,
        routinesForSelectedSite
    ) {
        val activeRoutineIsAvailable =
            activeRoutine == null ||
                routinesForSelectedSite.any {
                    it.id == activeRoutine.id
                }

        if (!activeRoutineIsAvailable) {
            routineStorage.setActiveRoutine(userId, null)
        }
    }

    val metersForSite = remember(baseMeters, selectedSite) {
        baseMeters.filter { it.site == selectedSite }
    }

    val activeRoutineMeters = remember(activeRoutine, meterById) {
        activeRoutine
            ?.meterIds
            ?.mapNotNull { meterById[it] }
            .orEmpty()
    }

    val effectiveMeters = remember(
        activeRoutine,
        metersForSite,
        meterById
    ) {
        val routineMeters = activeRoutine
            ?.meterIds
            ?.mapNotNull { meterById[it] }
            .orEmpty()

        if (activeRoutine == null) {
            metersForSite
        } else {
            routineMeters.filter { it.site == selectedSite }
        }
    }

    var selectedMeter by remember {
        mutableStateOf(
            effectiveMeters.firstOrNull()
                ?: metersForSite.firstOrNull()
                ?: baseMeters.first()
        )
    }

    LaunchedEffect(effectiveMeters, metersForSite, baseMeters) {
        val fallback = effectiveMeters.firstOrNull()
            ?: metersForSite.firstOrNull()
            ?: baseMeters.firstOrNull()

        if (
            fallback != null &&
            effectiveMeters.none { it.id == selectedMeter.id }
        ) {
            selectedMeter = fallback
        }
    }

    val lastValueByMeter = remember(readings) {
        readings
            .groupBy { it.meterId }
            .mapValues { (_, list) ->
                list.maxByOrNull { it.timestampMs }?.value
            }
    }

    val todayStart = remember {
        startOfTodayMs()
    }

    val todaysReadings = remember(readings, enteredBy) {
        readings.filter {
            it.enteredBy == enteredBy &&
                it.timestampMs >= todayStart
        }
    }

    val todaysMeterIds = remember(todaysReadings) {
        todaysReadings.map { it.meterId }.toSet()
    }

    val isRoutineDoneToday = remember(
        activeRoutine,
        activeRoutineMeters,
        todaysMeterIds,
        selectedSite
    ) {
        if (
            activeRoutine == null ||
            activeRoutineMeters.isEmpty()
        ) {
            false
        } else {
            activeRoutineMeters
                .filter { it.site == selectedSite }
                .all { it.id in todaysMeterIds }
        }
    }

    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val valueFocusRequester = remember { FocusRequester() }

    var isSaving by remember { mutableStateOf(false) }
    var isSending by remember {mutableStateOf(false)}
    var valueError by remember { mutableStateOf<String?>(null) }
    var valueText by remember { mutableStateOf("") }
    var valueDirty by remember { mutableStateOf(false) }
    var meterExpanded by remember { mutableStateOf(false) }
    var routineExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(selectedMeter.id) {
        val last = lastValueByMeter[selectedMeter.id]

        if (!valueDirty) {
            valueText = if (last != null) {
                formatValue(selectedMeter, last)
            } else {
                ""
            }
        }
    }

    val dateFormat = remember {
        SimpleDateFormat(
            "dd.MM HH:mm",
            Locale.getDefault()
        )
    }

    fun sendAll() {
        if (isSending) return

        if (readings.isEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar("Ni vnosov za pošiljanje.")
            }
            return
        }

        isSending = true
        scope.launch {
            val result = vm.postReadingsToServer(readings)
            isSending = false

            result.fold(
                onSuccess = {
                    snackbarHostState.showSnackbar(
                        "Uspešno poslano na strežnik (${readings.size} zapisov)!"
                    )
                },
                onFailure = { error ->
                    snackbarHostState.showSnackbar(
                        "Napaka pri pošiljanju: ${error.localizedMessage ?: error.message ?: "Neznana napaka"}"
                    )
                }
            )
        }
    }

    fun saveCurrent() {
        if (isSaving) return

        if (!isValidForMeter(selectedMeter, valueText)) {
            valueError = if (selectedMeter.decimals == 0) {
                "Dovoljene so samo cele številke."
            } else {
                "Dovoljena je največ 1 decimalka (npr. 1234.5)."
            }
            return
        }

        val value = valueText
            .trim()
            .replace(',', '.')
            .toDoubleOrNull()

        if (value == null) {
            valueError = "Neveljavna številka."
            return
        }

        isSaving = true
        valueError = null

        vm.addReading(
            Reading(
                id = UUID.randomUUID().toString(),
                meterId = selectedMeter.id,
                value = value,
                timestampMs = System.currentTimeMillis(),
                enteredBy = enteredBy
            )
        )

        val index = effectiveMeters.indexOfFirst {
            it.id == selectedMeter.id
        }

        val isRoutineMode =
            activeRoutine != null &&
                effectiveMeters.isNotEmpty()

        val isLastInList =
            index == effectiveMeters.lastIndex

        valueText = ""
        valueDirty = false

        keyboard?.hide()
        focusManager.clearFocus()
        isSaving = false

        scope.launch {
            if (
                effectiveMeters.isNotEmpty() &&
                index >= 0
            ) {
                if (isRoutineMode && isLastInList) {
                    snackbarHostState.showSnackbar(
                        activeRoutine?.name?.let {
                            "Rutina \"$it\" opravljena!"
                        } ?: "Rutina opravljena!"
                    )

                    selectedMeter = effectiveMeters.first()
                } else {
                    selectedMeter =
                        effectiveMeters[
                            (index + 1) % effectiveMeters.size
                        ]

                    snackbarHostState.showSnackbar(
                        "Vnos shranjen"
                    )
                }
            } else {
                snackbarHostState.showSnackbar(
                    "Vnos shranjen"
                )
            }

            valueFocusRequester.requestFocus()
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MeterEntryHeader(
                enteredBy = enteredBy,
                isAdmin = isAdmin,
                onOpenSettings = onOpenSettings,
                onLogout = onLogout,
                onAdminHistory = onAdminHistory
            )

            MeterViewSelector(
                sites = sites,
                selectedSite = selectedSite,
                activeRoutineName = activeRoutine?.name,
                routines = routinesForSelectedSite,
                routineDoneToday = isRoutineDoneToday,
                routineExpanded = routineExpanded,
                onRoutineExpandedChange = {
                    routineExpanded = it
                },
                onSiteSelected = { site ->
                    selectedSite = site
                    valueDirty = false
                },
                onAllMetersSelected = {
                    scope.launch {
                        routineStorage.setActiveRoutine(
                            userId,
                            null
                        )
                    }

                    routineExpanded = false
                    valueDirty = false
                },
                onRoutineSelected = { routineId ->
                    scope.launch {
                        routineStorage.setActiveRoutine(
                            userId,
                            routineId
                        )
                    }

                    routineExpanded = false
                    valueDirty = false
                }
            )

            val emptyMessage = when {
                activeRoutine != null &&
                    routines.isEmpty() ->
                    "Pojdi v Nastavitve in ustvari vsaj eno rutino."

                selectedSite.isNotBlank() &&
                    effectiveMeters.isEmpty() &&
                    activeRoutine != null ->
                    "Izbrana rutina za lokacijo $selectedSite ne vsebuje nobenega števca."

                selectedSite.isNotBlank() &&
                    effectiveMeters.isEmpty() ->
                    "Za lokacijo $selectedSite trenutno ni števcev."

                else -> null
            }

            if (emptyMessage != null) {
                EmptyMetersCard(
                    title = if (
                        activeRoutine != null &&
                        routines.isEmpty()
                    ) {
                        "Ni še ustvarjenih rutin."
                    } else {
                        "Ni števcev za prikaz."
                    },
                    message = emptyMessage
                )
            } else {
                MeterInputSection(
                    selectedMeter = selectedMeter,
                    meters = effectiveMeters,
                    meterExpanded = meterExpanded,
                    valueText = valueText,
                    valueError = valueError,
                    isSaving = isSaving,
                    isSending = isSending,
                    focusRequester = valueFocusRequester,
                    onMeterExpandedChange = {
                        meterExpanded = it
                    },
                    onMeterSelected = { meter ->
                        selectedMeter = meter
                        meterExpanded = false
                        valueDirty = false
                    },
                    onValueChange = {
                        valueText = it.replace(',', '.')
                        valueDirty = true
                        valueError = null
                    },
                    onSave = ::saveCurrent,
                    onSend = ::sendAll,
                    isValueValid = isValidForMeter(
                        selectedMeter,
                        valueText
                    )
                )
            }

            RecentReadingsSection(
                readings = readings,
                meterById = meterById,
                listState = listState,
                formatTimestamp = {
                    dateFormat.format(Date(it))
                },
                formatValue = ::formatValue
            )
        }
    }
}

private fun startOfTodayMs(): Long {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}

private fun formatValue(
    meter: Meter,
    value: Double
): String {
    return BigDecimal(value)
        .setScale(
            meter.decimals,
            RoundingMode.HALF_UP
        )
        .toPlainString()
}

private fun isValidForMeter(
    meter: Meter,
    text: String
): Boolean {
    val trimmed = text.trim()

    if (trimmed.isEmpty()) return false

    val regex = if (meter.decimals == 0) {
        Regex("""^\d+$""")
    } else {
        Regex("""^\d+(\.\d)?$""")
    }

    return regex.matches(trimmed)
}
