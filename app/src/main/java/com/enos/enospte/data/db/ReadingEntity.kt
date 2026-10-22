package com.enos.enospte.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "readings",
    indices = [Index(value = ["timestampMs"])]
)
data class ReadingEntity(
    @PrimaryKey val id: String,
    val meterId: String,
    val value: Double,
    val timestampMs: Long,
    val enteredBy: String
)
