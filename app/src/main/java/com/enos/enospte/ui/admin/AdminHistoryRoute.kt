package com.enos.enospte.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.enos.enospte.data.source.MeterCatalog
import com.enos.enospte.viewmodel.MainViewModel

@Composable
fun AdminHistoryRoute(
    vm: MainViewModel,
    onBack: () -> Unit
) {
    val readings by vm.readings.collectAsState()
    val activityLogs by vm.activityLogs.collectAsState()
    val meters = MeterCatalog.getMeters()

    AdminHistoryScreen(
        readings = readings,
        activityLogs = activityLogs,
        meters = meters,
        onBack = onBack,
        onDeleteAll = vm::deleteAllReadings
    )
}