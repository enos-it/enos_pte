package com.enos.enospte.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enos.enospte.data.db.DatabaseProvider
import com.enos.enospte.data.model.ActivityLog
import com.enos.enospte.data.model.Reading
import com.enos.enospte.data.repository.ActivityLogRepository
import com.enos.enospte.data.repository.ReadingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val db by lazy { DatabaseProvider.get(app) }

    private val readingRepo: ReadingRepository by lazy {
        ReadingRepository(db.readingDao(), db.activityLogDao())
    }

    private val activityRepo: ActivityLogRepository by lazy {
        ActivityLogRepository(db.activityLogDao())
    }

    val readings: StateFlow<List<Reading>> =
        readingRepo.readings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val activityLogs: StateFlow<List<ActivityLog>> =
        activityRepo.events.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addReading(reading: Reading) {
        viewModelScope.launch { readingRepo.addReading(reading) }
    }

    fun deleteReading(id: String) {
        viewModelScope.launch { readingRepo.deleteReading(id) }
    }

    fun updateReading(reading: Reading) {
        viewModelScope.launch { readingRepo.updateReading(reading) }
    }

    fun deleteAllReadings() {
        viewModelScope.launch { readingRepo.deleteAllReadings() }
    }

    suspend fun postReadingsToServer(readingsList: List<Reading>): Result<Unit> {
        return readingRepo.postReadingsToServer(readingsList)
    }
}