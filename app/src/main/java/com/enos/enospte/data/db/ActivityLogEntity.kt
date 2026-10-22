package com.enos.enospte.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activity_log",
    indices = [
        Index(value = ["timestampMs"]),
        Index(value = ["userId"]),
        Index(value = ["type"])
    ]
)
data class ActivityLogEntity(
    @PrimaryKey val id: String,
    val timestampMs: Long,
    val userId: String,
    val userName: String,
    val type: String,          // npr. "PIN_CHANGED", "READING_SAVED"
    val message: String,       // kratko, za seznam
    val detailsJson: String? = null // optional (prej/potem, meterIds, ipd.)
)