package com.enos.enospte.ui.meters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enos.enospte.data.datastore.MeterRoutineConfig
import com.enos.enospte.data.model.Meter
import com.enos.enospte.ui.meters.components.ExpandableGroupRow
import com.enos.enospte.ui.meters.components.ExpandableSubgroupRow
import com.enos.enospte.ui.meters.components.MeterSearchField
import com.enos.enospte.ui.meters.components.MeterSelectionRow
import com.enos.enospte.ui.meters.components.RoutineOrderRow
import com.enos.enospte.ui.meters.components.SiteSelector
import com.enos.enospte.viewmodel.MeterRoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterOrderScreen(
    userId: String,
    routineId: String,
    meters: List<Meter>,
    onBack: () -> Unit
) {
    val vm: MeterRoutineViewModel = viewModel()

    val config by vm.observeConfig(userId).collectAsState(
        initial = MeterRoutineConfig()
    )

    val routine = remember(config, routineId) {
        config.routines.firstOrNull { it.id == routineId }
    }

    val sites = remember(meters) {
        meters.map { it.site }.distinct().sorted()
    }

    var selectedSite by remember {
        mutableStateOf(sites.firstOrNull().orEmpty())
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val byId = remember(meters) {
        meters.associateBy { it.id }
    }

    val storedIds = remember(routine) {
        routine?.meterIds.orEmpty()
    }

    var workingRoutine by remember(routineId, storedIds, byId) {
        mutableStateOf(storedIds.mapNotNull { byId[it] })
    }

    LaunchedEffect(routineId, storedIds, byId) {
        workingRoutine = storedIds.mapNotNull { byId[it] }
    }

    val workingSet = remember(workingRoutine) {
        workingRoutine.map { it.id }.toSet()
    }

    val metersForSite = remember(meters, selectedSite) {
        meters.filter { it.site == selectedSite }
    }

    val filteredMetersForSite = remember(metersForSite, searchText) {
        val query = searchText.trim()

        if (query.isBlank()) {
            metersForSite
        } else {
            metersForSite.filter { meter ->
                meter.name.contains(query, ignoreCase = true) ||
                    meter.id.contains(query, ignoreCase = true) ||
                    meter.group.contains(query, ignoreCase = true) ||
                    meter.subgroup.contains(query, ignoreCase = true) ||
                    meter.unit.contains(query, ignoreCase = true)
            }
        }
    }

    val metersByGroup = remember(filteredMetersForSite) {
        filteredMetersForSite.groupBy {
            it.group.ifBlank { "OSTALO" }
        }
    }

    val isDirty = remember(workingRoutine, storedIds) {
        workingRoutine.map { it.id } != storedIds
    }

    val expandedGroups = remember {
        mutableStateMapOf<String, Boolean>()
    }

    val expandedSubgroups = remember {
        mutableStateMapOf<String, Boolean>()
    }

    fun updateSingleMeter(
        meter: Meter,
        checked: Boolean
    ) {
        workingRoutine = if (checked) {
            addMetersInCatalogOrder(
                current = workingRoutine,
                metersToAdd = listOf(meter),
                catalog = meters
            )
        } else {
            removeMeters(
                current = workingRoutine,
                metersToRemove = listOf(meter)
            )
        }
    }

    fun selectAll(targetMeters: List<Meter>) {
        workingRoutine = addMetersInCatalogOrder(
            current = workingRoutine,
            metersToAdd = targetMeters,
            catalog = meters
        )
    }

    fun removeAll(targetMeters: List<Meter>) {
        workingRoutine = removeMeters(
            current = workingRoutine,
            metersToRemove = targetMeters
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(routine?.name ?: "Rutina")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Nazaj"
                        )
                    }
                },
                actions = {
                    TextButton(
                        enabled = isDirty && routine != null,
                        onClick = {
                            val currentRoutine = routine
                                ?: return@TextButton

                            vm.upsertRoutine(
                                userId = userId,
                                routineId = currentRoutine.id,
                                routineName = currentRoutine.name,
                                ids = workingRoutine.map { it.id }
                            )
                        }
                    ) {
                        Text("Shrani")
                    }

                    TextButton(
                        enabled = workingRoutine.isNotEmpty() && routine != null,
                        onClick = {
                            workingRoutine = emptyList()
                        }
                    ) {
                        Text("Počisti")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SiteSelector(
                sites = sites,
                selectedSite = selectedSite,
                onSiteSelected = { site ->
                    selectedSite = site
                    searchText = ""
                }
            )

            Text("1) Rutina, po kateri gre auto-next:")

            if (workingRoutine.isEmpty()) {
                Text("Rutina je prazna. Spodaj dodaj vsaj en števec.")
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = workingRoutine,
                        key = { _, meter -> meter.id }
                    ) { index, meter ->
                        RoutineOrderRow(
                            meter = meter,
                            canMoveUp = index > 0,
                            canMoveDown = index < workingRoutine.lastIndex,
                            onMoveUp = {
                                val list = workingRoutine.toMutableList()
                                val previous = list[index - 1]

                                list[index - 1] = list[index]
                                list[index] = previous
                                workingRoutine = list
                            },
                            onMoveDown = {
                                val list = workingRoutine.toMutableList()
                                val next = list[index + 1]

                                list[index + 1] = list[index]
                                list[index] = next
                                workingRoutine = list
                            }
                        )
                    }
                }
            }

            HorizontalDivider()

            Text("2) Dodaj/odstrani števce za lokacijo: $selectedSite")

            MeterSearchField(
                value = searchText,
                onValueChange = { searchText = it },
                onClear = { searchText = "" }
            )

            if (filteredMetersForSite.isEmpty()) {
                Text("Za iskanje »${searchText.trim()}« ni najdenih števcev.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    metersByGroup.forEach { (groupName, groupMeters) ->
                        val groupKey = "$selectedSite|$groupName"
                        val searchActive = searchText.isNotBlank()
                        val groupExpanded =
                            searchActive || expandedGroups[groupKey] == true

                        val selectedInGroup = groupMeters.count {
                            it.id in workingSet
                        }

                        item(
                            key = "group_$groupKey"
                        ) {
                            ExpandableGroupRow(
                                name = groupName,
                                selectedCount = selectedInGroup,
                                totalCount = groupMeters.size,
                                expanded = groupExpanded,
                                onToggle = {
                                    if (!searchActive) {
                                        expandedGroups[groupKey] =
                                            expandedGroups[groupKey] != true
                                    }
                                },
                                onSelectAll = {
                                    selectAll(groupMeters)
                                },
                                onRemoveAll = {
                                    removeAll(groupMeters)
                                }
                            )
                        }

                        if (groupExpanded) {
                            val directMeters = groupMeters.filter {
                                it.subgroup.isBlank()
                            }

                            directMeters.forEach { meter ->
                                item(
                                    key = "meter_${meter.id}"
                                ) {
                                    MeterSelectionRow(
                                        meter = meter,
                                        checked = meter.id in workingSet,
                                        indentLevel = 1,
                                        onCheckedChange = { checked ->
                                            updateSingleMeter(meter, checked)
                                        }
                                    )
                                }
                            }

                            val metersBySubgroup = groupMeters
                                .filter { it.subgroup.isNotBlank() }
                                .groupBy { it.subgroup }

                            metersBySubgroup.forEach {
                                    (subgroupName, subgroupMeters) ->

                                val subgroupKey =
                                    "$selectedSite|$groupName|$subgroupName"

                                val subgroupExpanded =
                                    searchActive ||
                                        expandedSubgroups[subgroupKey] == true

                                val selectedInSubgroup =
                                    subgroupMeters.count {
                                        it.id in workingSet
                                    }

                                item(
                                    key = "subgroup_$subgroupKey"
                                ) {
                                    ExpandableSubgroupRow(
                                        name = subgroupName,
                                        selectedCount = selectedInSubgroup,
                                        totalCount = subgroupMeters.size,
                                        expanded = subgroupExpanded,
                                        onToggle = {
                                            if (!searchActive) {
                                                expandedSubgroups[subgroupKey] =
                                                    expandedSubgroups[subgroupKey] != true
                                            }
                                        },
                                        onSelectAll = {
                                            selectAll(subgroupMeters)
                                        },
                                        onRemoveAll = {
                                            removeAll(subgroupMeters)
                                        }
                                    )
                                }

                                if (subgroupExpanded) {
                                    subgroupMeters.forEach { meter ->
                                        item(
                                            key = "meter_${meter.id}"
                                        ) {
                                            MeterSelectionRow(
                                                meter = meter,
                                                checked = meter.id in workingSet,
                                                indentLevel = 2,
                                                onCheckedChange = { checked ->
                                                    updateSingleMeter(
                                                        meter = meter,
                                                        checked = checked
                                                    )
                                                }
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
    }
}

private fun addMetersInCatalogOrder(
    current: List<Meter>,
    metersToAdd: List<Meter>,
    catalog: List<Meter>
): List<Meter> {
    val currentIds = current.map { it.id }.toSet()
    val idsToAdd = metersToAdd.map { it.id }.toSet()

    val newlyAdded = catalog.filter { meter ->
        meter.id in idsToAdd && meter.id !in currentIds
    }

    return current + newlyAdded
}

private fun removeMeters(
    current: List<Meter>,
    metersToRemove: List<Meter>
): List<Meter> {
    val idsToRemove = metersToRemove.map { it.id }.toSet()

    return current.filterNot { it.id in idsToRemove }
}
