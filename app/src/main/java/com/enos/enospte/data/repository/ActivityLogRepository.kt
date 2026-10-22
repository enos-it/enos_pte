package com.enos.enospte.data.repository

import com.enos.enospte.data.db.ActivityLogDao
import com.enos.enospte.data.db.ActivityLogEntity
import com.enos.enospte.data.model.ActivityLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ActivityLogRepository(
    private val dao: ActivityLogDao
) {
    val events: Flow<List<ActivityLog>> =
        dao.observeAll().map { list -> list.map { it.toModel() } }

    suspend fun log(entity: ActivityLogEntity) {
        dao.insert(entity)
    }

    suspend fun clearAll() {
        dao.deleteAll()
    }
}

private fun ActivityLogEntity.toModel(): ActivityLog = ActivityLog(
    id = id,
    timestampMs = timestampMs,
    userId = userId,
    userName = userName,
    type = type,
    message = message,
    detailsJson = detailsJson
)