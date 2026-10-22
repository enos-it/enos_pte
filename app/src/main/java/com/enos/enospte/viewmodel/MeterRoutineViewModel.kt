package com.enos.enospte.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enos.enospte.data.datastore.MeterRoutineConfig
import com.enos.enospte.data.datastore.MeterRoutineDefinition
import com.enos.enospte.data.datastore.MeterRoutineStorage
import com.enos.enospte.data.db.ActivityLogEntity
import com.enos.enospte.data.db.DatabaseProvider
import com.enos.enospte.data.model.ActivityEventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class MeterRoutineViewModel(app: Application) : AndroidViewModel(app) {

    private val storage = MeterRoutineStorage(app.applicationContext)
    private val activityDao = DatabaseProvider.get(app).activityLogDao()

    fun observeConfig(userId: String): Flow<MeterRoutineConfig> =
        storage.observeConfig(userId)

    fun upsertRoutine(
        userId: String,
        routineId: String,
        routineName: String,
        ids: List<String>
    ) {
        viewModelScope.launch {
            storage.saveRoutine(
                userId = userId,
                routine = MeterRoutineDefinition(
                    id = routineId,
                    name = routineName,
                    meterIds = ids
                )
            )

            activityDao.insert(
                ActivityLogEntity(
                    id = UUID.randomUUID().toString(),
                    timestampMs = System.currentTimeMillis(),
                    userId = userId,
                    userName = userId,
                    type = ActivityEventType.ROUTINE_CHANGED,
                    message = "Routine saved",
                    detailsJson = """{"routineId":"$routineId","name":"$routineName","count":${ids.size}}"""
                )
            )
        }
    }

    fun deleteRoutine(userId: String, routineId: String) {
        viewModelScope.launch {
            storage.deleteRoutine(userId, routineId)

            activityDao.insert(
                ActivityLogEntity(
                    id = UUID.randomUUID().toString(),
                    timestampMs = System.currentTimeMillis(),
                    userId = userId,
                    userName = userId,
                    type = ActivityEventType.ROUTINE_DELETED,
                    message = "Routine deleted",
                    detailsJson = """{"routineId":"$routineId"}"""
                )
            )
        }
    }

    fun setActiveRoutine(userId: String, routineId: String?) {
        viewModelScope.launch {
            storage.setActiveRoutine(userId, routineId)

            activityDao.insert(
                ActivityLogEntity(
                    id = UUID.randomUUID().toString(),
                    timestampMs = System.currentTimeMillis(),
                    userId = userId,
                    userName = userId,
                    type = ActivityEventType.ACTIVE_ROUTINE_CHANGED,
                    message = "Active routine changed",
                    detailsJson = if (routineId != null) {
                        """{"routineId":"$routineId"}"""
                    } else {
                        """{}"""
                    }
                )
            )
        }
    }

}