package com.enos.enospte.data.repository

import com.enos.enospte.data.db.ActivityLogDao
import com.enos.enospte.data.db.ActivityLogEntity
import com.enos.enospte.data.db.ReadingDao
import com.enos.enospte.data.db.ReadingEntity
import com.enos.enospte.data.model.ActivityEventType
import com.enos.enospte.data.model.Reading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class ReadingRepository(
    private val dao: ReadingDao,
    private val activityDao: ActivityLogDao
) {
    val readings: Flow<List<Reading>> =
        dao.observeAll().map { list -> list.map { it.toModel() } }

    suspend fun addReading(reading: Reading) {
        dao.upsert(reading.toEntity())

        activityDao.insert(
            ActivityLogEntity(
                id = UUID.randomUUID().toString(),
                timestampMs = System.currentTimeMillis(),
                userId = reading.enteredBy,
                userName = reading.enteredBy,
                type = ActivityEventType.READING_SAVED,
                message = "${reading.meterId} = ${reading.value}"
            )
        )
    }

    suspend fun deleteReading(readingId: String, actorUserId: String = "admin") {
        dao.deleteById(readingId)
        activityDao.insert(
            ActivityLogEntity(
                id = UUID.randomUUID().toString(),
                timestampMs = System.currentTimeMillis(),
                userId = actorUserId,
                userName = actorUserId,
                type = ActivityEventType.READING_DELETED,
                message = "Deleted reading id=$readingId"
            )
        )
    }

    suspend fun updateReading(updated: Reading) {
        dao.upsert(updated.toEntity())
        activityDao.insert(
            ActivityLogEntity(
                id = UUID.randomUUID().toString(),
                timestampMs = System.currentTimeMillis(),
                userId = updated.enteredBy,
                userName = updated.enteredBy,
                type = ActivityEventType.READING_UPDATED,
                message = "${updated.meterId} = ${updated.value}"
            )
        )
    }

    suspend fun deleteAllReadings(actorUserId: String = "admin") {
        dao.deleteAll()
        activityDao.insert(
            ActivityLogEntity(
                id = UUID.randomUUID().toString(),
                timestampMs = System.currentTimeMillis(),
                userId = actorUserId,
                userName = actorUserId,
                type = ActivityEventType.READINGS_DELETE_ALL,
                message = "Deleted ALL readings"
            )
        )
    }
}

private fun ReadingEntity.toModel(): Reading = Reading(
    id = id,
    meterId = meterId,
    value = value,
    timestampMs = timestampMs,
    enteredBy = enteredBy
)

private fun Reading.toEntity(): ReadingEntity = ReadingEntity(
    id = id,
    meterId = meterId,
    value = value,
    timestampMs = timestampMs,
    enteredBy = enteredBy
)