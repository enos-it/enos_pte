package com.enos.enospte.data.model

data class Reading(
    val id: String,            // UUID - unikatni ID zapisa (primary key na serverju)
    val meterId: String,        // povezava na Meter.id
    val value: Double,          // vnesena vrednost
    val timestampMs: Long,      // System.currentTimeMillis()
    val enteredBy: String       // npr. "Jure" ali userId
)

data class DatabaseReading(
    val id: String,

    val siteId: Int,
    val groupId: Int,
    val subGroupId: Int,
    val unitId: Int,
    val meterNameId: Int,

    val value: Double,
    val timestampMs: Long,
    val enteredBy: String
)