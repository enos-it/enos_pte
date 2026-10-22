package com.enos.enospte.data.model

data class ActivityLog(
    val id: String,
    val timestampMs: Long,
    val userId: String,
    val userName: String,
    val type: String,
    val message: String,
    val detailsJson: String? = null
)