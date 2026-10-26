package com.enos.enospte.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enos.enospte.data.datastore.MeterRoutineConfig
import com.enos.enospte.data.datastore.MeterRoutineDefinition
import com.enos.enospte.data.source.UserCatalog
import com.enos.enospte.viewmodel.MeterRoutineViewModel
import com.enos.enospte.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userId: String,
    enteredBy: String,
    isAdmin: Boolean,
    showRoutineSettings: Boolean = true,
    onBack: () -> Unit,
    onMeterOrder: (String) -> Unit = {}
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var newPin by rememberSaveable(showPinDialog) { mutableStateOf("") }
    var pinError by rememberSaveable(showPinDialog) { mutableStateOf<String?>(null) }

    var showRoutines by rememberSaveable { mutableStateOf(false) }

    var showAddRoutineDialog by remember { mutableStateOf(false) }
    var newRoutineName by rememberSaveable(showAddRoutineDialog) { mutableStateOf("") }
    var newRoutineError by rememberSaveable(showAddRoutineDialog) { mutableStateOf<String?>(null) }

    var showResetDialog by remember { mutableStateOf(false) }
    var isResetting by remember { mutableStateOf(false) }

    var routineToDelete by remember { mutableStateOf<MeterRoutineDefinition?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val vm: SettingsViewModel = viewModel()
    val routineVm: MeterRoutineViewModel = viewModel()

    val storedPin by vm.observePin(userId).collectAsState(initial = null)
    val routineConfig by routineVm.observeConfig(userId).collectAsState(
        initial = MeterRoutineConfig()
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Nastavitve") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Nazaj")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Prijavljen uporabnik", style = MaterialTheme.typography.labelLarge)
                        Text(enteredBy, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }

            Text("Nastavitve", style = MaterialTheme.typography.titleMedium)

            SettingsItem(
                icon = { Icon(Icons.Default.Key, contentDescription = null) },
                title = "Spremeni PIN",
                subtitle = if (storedPin != null)
                    "PIN je nastavljen"
                else
                    "Nastavi nov 4-mestni PIN",
                onClick = { showPinDialog = true }
            )

            SettingsItem(
                icon = { Icon(Icons.Default.Refresh, contentDescription = null) },
                title = "Ponastavi podatke",
                subtitle = "Izbriši lokalne vnose in jih prenesi s strežnika",
                onClick = { showResetDialog = true }
            )

            if (showRoutineSettings) {
                SettingsItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    title = "Rutine",
                    subtitle = "Dodaj in uredi auto-next rutine.",
                    onClick = { showRoutines = !showRoutines }
                )
            }

            if (showRoutineSettings && showRoutines) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("Rutine", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Dodaj in uredi auto-next rutine.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            TextButton(onClick = { showAddRoutineDialog = true }) {
                                Text("+ Dodaj")
                            }
                        }

                        if (routineConfig.routines.isEmpty()) {
                            Text("Ni še ustvarjenih rutin.")
                        } else {
                            routineConfig.routines.forEach { routine ->
                                RoutinePreviewItem(
                                    name = routine.name,
                                    count = routine.meterIds.size,
                                    isActive = routine.id == routineConfig.activeRoutineId,
                                    onOpen = { onMeterOrder(routine.id) },
                                    onSetActive = {
                                        routineVm.setActiveRoutine(userId, routine.id)
                                    },
                                    onDelete = {
                                        routineToDelete = routine
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "ENOS PTE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    if (routineToDelete != null) {
        val routine = routineToDelete!!

        AlertDialog(
            onDismissRequest = { routineToDelete = null },
            title = { Text("Izbriši rutino") },
            text = {
                Text("Ali želiš izbrisati rutino \"${routine.name}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        routineVm.deleteRoutine(userId, routine.id)
                        routineToDelete = null
                    }
                ) {
                    Text("Izbriši")
                }
            },
            dismissButton = {
                TextButton(onClick = { routineToDelete = null }) {
                    Text("Prekliči")
                }
            }
        )
    }

    if (showAddRoutineDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddRoutineDialog = false
                newRoutineName = ""
                newRoutineError = null
            },
            title = { Text("Dodaj rutino") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newRoutineName,
                        onValueChange = {
                            newRoutineName = it
                            newRoutineError = null
                        },
                        label = { Text("Ime rutine") },
                        singleLine = true,
                        isError = newRoutineError != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    newRoutineError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val name = newRoutineName.trim()

                        if (name.isBlank()) {
                            newRoutineError = "Vpiši ime rutine."
                            return@TextButton
                        }

                        if (routineConfig.routines.any { it.name.equals(name, ignoreCase = true) }) {
                            newRoutineError = "Rutina s tem imenom že obstaja."
                            return@TextButton
                        }

                        val id = "routine_" + UUID.randomUUID().toString()

                        routineVm.upsertRoutine(
                            userId = userId,
                            routineId = id,
                            routineName = name,
                            ids = emptyList()
                        )

                        showAddRoutineDialog = false
                        newRoutineName = ""
                        newRoutineError = null
                    }
                ) {
                    Text("Dodaj")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddRoutineDialog = false
                        newRoutineName = ""
                        newRoutineError = null
                    }
                ) {
                    Text("Prekliči")
                }
            }
        )
    }

    if (showPinDialog) {
        var resetUserExpanded by remember { mutableStateOf(false) }
        var resetTargetId by remember { mutableStateOf(userId) }

        val allUsers = remember { UserCatalog.getUsers() }
        val resetTarget = allUsers.firstOrNull { it.id == resetTargetId }

        AlertDialog(
            onDismissRequest = {
                showPinDialog = false
                newPin = ""
                pinError = null
            },
            title = { Text("Spremeni PIN") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    if (isAdmin) {
                        ExposedDropdownMenuBox(
                            expanded = resetUserExpanded,
                            onExpandedChange = { resetUserExpanded = !resetUserExpanded }
                        ) {
                            OutlinedTextField(
                                value = resetTarget?.displayName ?: resetTargetId,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Uporabnik") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = resetUserExpanded
                                    )
                                },
                                modifier = Modifier
                                    .menuAnchor(
                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    )
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = resetUserExpanded,
                                onDismissRequest = { resetUserExpanded = false }
                            ) {
                                allUsers.forEach { u ->
                                    DropdownMenuItem(
                                        text = { Text("${u.displayName} (${u.id})") },
                                        onClick = {
                                            resetTargetId = u.id
                                            resetUserExpanded = false
                                            newPin = ""
                                            pinError = null
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = newPin,
                        onValueChange = {
                            newPin = it.filter { ch -> ch.isDigit() }.take(4)
                            pinError = null
                        },
                        label = { Text("Nov PIN (4 številke)") },
                        singleLine = true,
                        isError = pinError != null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    pinError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newPin.length != 4) {
                        pinError = "PIN mora imeti 4 številke."
                        return@TextButton
                    }

                    val targetId = if (isAdmin) resetTargetId else userId

                    vm.setPin(targetId, newPin)

                    showPinDialog = false
                    newPin = ""
                    pinError = null
                }) {
                    Text("Shrani")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPinDialog = false
                    newPin = ""
                    pinError = null
                }) {
                    Text("Prekliči")
                }
            }
        )
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isResetting) showResetDialog = false
            },
            title = { Text("Ponastavi podatke") },
            text = {
                Text("Ali res želiš izbrisati vse lokalne vnose za uporabnika \"$enteredBy\" in jih ponovno prenesti s strežnika?")
            },
            confirmButton = {
                TextButton(
                    enabled = !isResetting,
                    onClick = {
                        isResetting = true
                        vm.resetUserReadings(userId) { result ->
                            isResetting = false
                            showResetDialog = false
                            scope.launch {
                                result.fold(
                                    onSuccess = { count ->
                                        snackbarHostState.showSnackbar("Ponastavljeno! Naloženih $count zapisov.")
                                    },
                                    onFailure = { error ->
                                        snackbarHostState.showSnackbar(
                                            "Napaka pri ponastavitvi: ${error.localizedMessage ?: error.message}"
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) {
                    Text(if (isResetting) "Nalaganje..." else "Ponastavi")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isResetting,
                    onClick = { showResetDialog = false }
                ) {
                    Text("Prekliči")
                }
            }
        )
    }
}

@Composable
private fun RoutinePreviewItem(
    name: String,
    count: Int,
    isActive: Boolean,
    onOpen: () -> Unit,
    onSetActive: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = if (isActive) {
                        "$count števcev • aktivna"
                    } else {
                        "$count števcev"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (!isActive) {
                    TextButton(onClick = onSetActive) {
                        Text("Nastavi")
                    }
                }

                TextButton(onClick = onOpen) {
                    Text("Uredi")
                }

                TextButton(onClick = onDelete) {
                    Text("Briši")
                }
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        ListItem(
            leadingContent = icon,
            headlineContent = { Text(title) },
            supportingContent = { Text(subtitle) },
            trailingContent = {
                Icon(Icons.Default.ChevronRight, contentDescription = null)
            }
        )
    }
}