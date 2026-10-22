package com.enos.enospte.ui.admin

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.enos.enospte.data.model.ActivityLog
import com.enos.enospte.data.model.ActivityEventType
import com.enos.enospte.data.model.Meter
import com.enos.enospte.data.model.Reading
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class TimeFilter(val label: String) {
    ALL("Vse"),
    TODAY("Danes"),
    DAYS_7("7 dni")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AdminHistoryScreen(
    readings: List<Reading>,
    activityLogs: List<ActivityLog>,
    meters: List<Meter>,
    onBack: () -> Unit,
    onDeleteAll: () -> Unit
) {
    val meterById = remember(meters) { meters.associateBy { it.id } }

    // --- filter options ---
    val userOptions = remember(readings, activityLogs) {
        val fromReadings = readings.map { it.enteredBy }
        val fromEvents = activityLogs.map { it.userName }
        listOf("Vsi") + (fromReadings + fromEvents).distinct().sorted()
    }
    val siteOptions = remember(meters) {
        listOf("Vsi") + meters.map { it.site }.distinct().sorted()
    }

    var timeFilter by remember { mutableStateOf(TimeFilter.ALL) }
    var selectedUser by remember { mutableStateOf("Vsi") }
    var selectedSite by remember { mutableStateOf("Vsi") }
    var search by remember { mutableStateOf(TextFieldValue("")) }

    var userExpanded by remember { mutableStateOf(false) }
    var siteExpanded by remember { mutableStateOf(false) }

    // --- Tabs ---
    var tab by remember { mutableIntStateOf(0) } // 0=Vpisi, 1=Dogodki

    // --- filtering helpers ---
    fun timeOk(ts: Long): Boolean = when (timeFilter) {
        TimeFilter.ALL -> true
        TimeFilter.TODAY -> ts >= startOfTodayMs()
        TimeFilter.DAYS_7 -> ts >= (System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000)
    }

    fun userOk(name: String): Boolean =
        selectedUser == "Vsi" || name == selectedUser

    fun siteOkReading(meterId: String): Boolean {
        if (selectedSite == "Vsi") return true
        val site = meterById[meterId]?.site
        return site == selectedSite
    }

    fun searchOkReading(meterId: String): Boolean {
        val q = search.text.trim()
        if (q.isEmpty()) return true
        val name = meterById[meterId]?.name ?: meterId
        return name.contains(q, ignoreCase = true)
    }

    // Events: site je “mehko” (ker eventi nimajo site polja).
    // Poskusimo najti site v message ali detailsJson (npr. {"site":"BELA"} ali "Order changed (BELA)").
    fun siteOkEvent(e: ActivityLog): Boolean {
        if (selectedSite == "Vsi") return true
        val s = selectedSite
        val msg = e.message
        val det = e.detailsJson
        return msg.contains("($s)", ignoreCase = true) ||
                msg.contains(s, ignoreCase = true) ||
                (det?.contains("\"site\":\"$s\"", ignoreCase = true) == true) ||
                (det?.contains(s, ignoreCase = true) == true)
    }

    fun searchOkEvent(e: ActivityLog): Boolean {
        val q = search.text.trim()
        if (q.isEmpty()) return true
        return e.type.contains(q, ignoreCase = true) ||
                e.message.contains(q, ignoreCase = true) ||
                (e.detailsJson?.contains(q, ignoreCase = true) == true)
    }

    val filteredReadings = remember(readings, meters, timeFilter, selectedUser, selectedSite, search) {
        readings
            .asSequence()
            .filter { timeOk(it.timestampMs) }
            .filter { userOk(it.enteredBy) }
            .filter { siteOkReading(it.meterId) }
            .filter { searchOkReading(it.meterId) }
            .sortedByDescending { it.timestampMs }
            .toList()
    }

    val filteredEvents = remember(activityLogs, timeFilter, selectedUser, selectedSite, search) {
        activityLogs
            .asSequence()
            .filter { timeOk(it.timestampMs) }
            .filter { userOk(it.userName) }
            .filter { siteOkEvent(it) }
            .filter { searchOkEvent(it) }
            .sortedByDescending { it.timestampMs }
            .toList()
    }

    // --- details dialog ---
    var selected by remember { mutableStateOf<Reading?>(null) }
    var showDeleteAllConfirm by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin – Aktivnost") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } },
                actions = {
                    TextButton(
                        onClick = { showDeleteAllConfirm = true },
                        enabled = readings.isNotEmpty()
                    ) { Text("Izbriši vse") }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // ---- Filter bar ----
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // časovni filter
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        TimeFilter.entries.forEachIndexed { idx, tf ->
                            SegmentedButton(
                                selected = timeFilter == tf,
                                onClick = { timeFilter = tf },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = idx,
                                    count = TimeFilter.entries.size
                                )
                            ) { Text(tf.label) }
                        }
                    }

                    // user + site dropdown
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                        ExposedDropdownMenuBox(
                            expanded = userExpanded,
                            onExpandedChange = { userExpanded = !userExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedUser,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Uporabnik") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userExpanded) },
                                modifier = Modifier
                                    .menuAnchor(
                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    )
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = userExpanded,
                                onDismissRequest = { userExpanded = false }
                            ) {
                                userOptions.forEach { u ->
                                    DropdownMenuItem(
                                        text = { Text(u) },
                                        onClick = {
                                            selectedUser = u
                                            userExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = siteExpanded,
                            onExpandedChange = { siteExpanded = !siteExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedSite,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Site") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = siteExpanded) },
                                modifier = Modifier
                                    .menuAnchor(
                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    )
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = siteExpanded,
                                onDismissRequest = { siteExpanded = false }
                            ) {
                                siteOptions.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s) },
                                        onClick = {
                                            selectedSite = s
                                            siteExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // search
                    OutlinedTextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Išči (ime / dogodek)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val shown = if (tab == 0) filteredReadings.size else filteredEvents.size
                    val total = if (tab == 0) readings.size else activityLogs.size
                    Text(
                        text = "Prikaz: $shown / $total",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // ---- Sticky tabs ----
            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    HorizontalDivider()
                    PrimaryTabRow(selectedTabIndex = tab) {
                        Tab(
                            selected = tab == 0,
                            onClick = { tab = 0 },
                            text = { Text("Vpisi") }
                        )
                        Tab(
                            selected = tab == 1,
                            onClick = { tab = 1 },
                            text = { Text("Dogodki") }
                        )
                    }
                }
            }

            // ---- Content ----
            if (tab == 0) {

                if (filteredReadings.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Ni zadetkov za izbrane filtre.")
                        }
                    }
                } else {
                    val grouped = filteredReadings.groupBy { dayKey(it.timestampMs) }
                    val dayOrder = grouped.keys.sortedDescending()

                    dayOrder.forEach { dk ->
                        val dayReadings = grouped[dk].orEmpty().sortedByDescending { it.timestampMs }

                        item(key = "rh_$dk") {
                            DayHeader(dayHeaderLabel(dayReadings.first().timestampMs))
                        }

                        items(dayReadings, key = { it.id }) { r ->
                            val m = meterById[r.meterId]
                            val meterName = m?.name ?: r.meterId
                            val unit = m?.unit?.takeIf { it.isNotBlank() } ?: ""
                            val valueLabel = formatValue(m, r.value)
                            val site = m?.site

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                                    .clickable { selected = r },
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(formatTime(r.timestampMs))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            if (!site.isNullOrBlank()) {
                                                Text(site, style = MaterialTheme.typography.labelMedium)
                                            }
                                            Text(r.enteredBy)
                                        }
                                    }

                                    Text(meterName, style = MaterialTheme.typography.titleMedium)

                                    Text(
                                        text = if (unit.isNotBlank()) "$valueLabel $unit" else valueLabel,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }
                }

            } else {

                if (filteredEvents.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Ni dogodkov za izbrane filtre.")
                        }
                    }
                } else {
                    val grouped = filteredEvents.groupBy { dayKey(it.timestampMs) }
                    val dayOrder = grouped.keys.sortedDescending()

                    dayOrder.forEach { dk ->
                        val dayEvents = grouped[dk].orEmpty().sortedByDescending { it.timestampMs }

                        item(key = "eh_$dk") {
                            DayHeader(dayHeaderLabel(dayEvents.first().timestampMs))
                        }

                        items(dayEvents, key = { it.id }) { e ->
                            val icon = typeIcon(e.type)
                            val title = prettyType(e.type)
                            val site = eventSiteLabel(e)

                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(formatTime(e.timestampMs))

                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            if (!site.isNullOrBlank()) {
                                                Text(site, style = MaterialTheme.typography.labelMedium)
                                            }
                                            Text(e.userName)
                                        }
                                    }

                                    Text(
                                        text = "$icon $title",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    // samo za readinge pokažemo opis (ne kažemo "PIN changed", "Routine updated", JSON, ...)
                                    if (e.type == ActivityEventType.READING_SAVED) {
                                        Text(
                                            prettyEventMessage(e, meterById),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ---- Delete all confirm dialog ----
    if (showDeleteAllConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteAllConfirm = false },
            title = { Text("Potrdi brisanje") },
            text = { Text("Res želiš izbrisati VSE vnose iz baze?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteAll()
                    showDeleteAllConfirm = false
                }) { Text("IZBRIŠI") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllConfirm = false }) { Text("Prekliči") }
            }
        )
    }

    // ---- Details dialog (read-only) ----
    selected?.let { r ->
        val m = meterById[r.meterId]
        val meterName = m?.name ?: r.meterId
        val unit = m?.unit?.takeIf { it.isNotBlank() } ?: ""
        val valueLabel = formatValue(m, r.value)

        AlertDialog(
            onDismissRequest = { selected = null },
            title = { Text("Podrobnosti") },
            text = {
                Text(
                    "Čas: ${formatTime(r.timestampMs)}\n" +
                            "Uporabnik: ${r.enteredBy}\n" +
                            "Site: ${m?.site ?: "?"}\n" +
                            "Števec: $meterName\n" +
                            "Vrednost: $valueLabel${if (unit.isNotBlank()) " $unit" else ""}\n" +
                            "ID: ${r.id}"
                )
            },
            confirmButton = {
                TextButton(onClick = { selected = null }) { Text("Zapri") }
            }
        )
    }
}

// ---------- helpers (MORAJO BITI IZVEN @Composable FUNKCIJE) ----------

private fun formatTime(tsMs: Long): String {
    val sdf = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())
    return sdf.format(Date(tsMs))
}

private fun startOfTodayMs(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private fun dayKey(tsMs: Long): String {
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return sdf.format(Date(tsMs))
}

private fun dayHeaderLabel(tsMs: Long): String {
    val cal = Calendar.getInstance()
    val todayKey = dayKey(cal.timeInMillis)

    cal.add(Calendar.DAY_OF_YEAR, -1)
    val yesterdayKey = dayKey(cal.timeInMillis)

    val k = dayKey(tsMs)
    return when (k) {
        todayKey -> "DANES"
        yesterdayKey -> "VČERAJ"
        else -> SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(tsMs))
    }
}

@Composable
private fun DayHeader(text: String) {
    Surface(
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

private fun formatValue(meter: Meter?, value: Double): String {
    if (meter == null) return value.toString()
    val bd = BigDecimal(value).setScale(meter.decimals, RoundingMode.HALF_UP)
    return bd.toPlainString()
}

private fun prettyType(type: String): String = when (type) {
    ActivityEventType.PIN_CHANGED -> "Spremenjen PIN"
    ActivityEventType.PIN_RESET -> "Ponastavljen PIN"

    "METER_ORDER_CHANGED" -> "Spremenjen vrstni red števcev"
    "METER_VISIBILITY_CHANGED" -> "Spremenjena vidnost števcev"
    "METER_SETTINGS_RESET" -> "Ponastavitev nastavitev števcev"

    ActivityEventType.ROUTINE_CHANGED -> "Posodobljena rutina"
    "ROUTINE_CREATED" -> "Ustvarjena rutina"
    ActivityEventType.ROUTINE_DELETED -> "Izbrisana rutina"
    "ROUTINE_CLEARED" -> "Izpraznjena rutina"
    ActivityEventType.ACTIVE_ROUTINE_CHANGED -> "Spremenjena aktivna rutina"
    "ROUTINE_MODE_CHANGED" -> "Spremenjen način rutine"

    ActivityEventType.READING_SAVED -> "Vpis števca"
    ActivityEventType.READING_DELETED -> "Izbris vpisa"
    ActivityEventType.READING_UPDATED -> "Popravek vpisa"
    ActivityEventType.READINGS_DELETE_ALL -> "Izbris vseh vpisov"

    ActivityEventType.USER_CREATED -> "Dodan uporabnik"
    ActivityEventType.USER_DELETED -> "Izbrisan uporabnik"
    "USER_DISABLED" -> "Uporabnik onemogočen"
    "USER_ENABLED" -> "Uporabnik omogočen"
    "USER_LOGIN" -> "Prijava"
    "USER_LOGOUT" -> "Odjava"

    else -> type
}

private fun typeIcon(type: String): String = when (type) {
    ActivityEventType.PIN_CHANGED, ActivityEventType.PIN_RESET -> "🔐"

    "METER_ORDER_CHANGED" -> "↕️"
    "METER_VISIBILITY_CHANGED" -> "👁️"
    "METER_SETTINGS_RESET" -> "🧽"

    ActivityEventType.ROUTINE_CHANGED,
    "ROUTINE_CREATED",
    ActivityEventType.ACTIVE_ROUTINE_CHANGED,
    "ROUTINE_MODE_CHANGED" -> "📋"

    ActivityEventType.ROUTINE_DELETED,
    "ROUTINE_CLEARED" -> "🗑️"

    ActivityEventType.READING_SAVED -> "📝"
    ActivityEventType.READING_DELETED, ActivityEventType.READING_UPDATED -> "✏️"
    ActivityEventType.READINGS_DELETE_ALL -> "🧹"

    ActivityEventType.USER_CREATED, "USER_ENABLED" -> "👤"
    ActivityEventType.USER_DELETED, "USER_DISABLED" -> "🚫"
    "USER_LOGIN" -> "✅"
    "USER_LOGOUT" -> "🚪"

    else -> "•"
}

private fun prettyEventMessage(e: ActivityLog, meterById: Map<String, Meter>): String {
    if (e.type != ActivityEventType.READING_SAVED) return e.message

    // pričakujemo format "meterId = value"
    val raw = e.message
    val parts = raw.split("=").map { it.trim() }
    if (parts.size < 2) return raw

    val meterId = parts[0]
    val value = parts[1]

    val m = meterById[meterId]
    val name = m?.name ?: meterId
    val unit = m?.unit?.takeIf { it.isNotBlank() } ?: ""

    return if (unit.isNotBlank()) "$name = $value $unit" else "$name = $value"
}

private fun eventSiteLabel(e: ActivityLog): String? {
    val d = e.detailsJson
    if (!d.isNullOrBlank()) {
        val m = Regex("\"site\"\\s*:\\s*\"([^\"]+)\"").find(d)
        if (m != null) return m.groupValues[1]
    }
    val m2 = Regex("\\(([^)]+)\\)").find(e.message)
    return m2?.groupValues?.getOrNull(1)
}