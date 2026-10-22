package com.enos.enospte.data.model

object ActivityEventType {
    const val PIN_CHANGED = "PIN_CHANGED"
    const val PIN_RESET = "PIN_RESET"

    const val ROUTINE_CHANGED = "ROUTINE_CHANGED"
    const val ROUTINE_DELETED = "ROUTINE_DELETED"
    const val ACTIVE_ROUTINE_CHANGED = "ACTIVE_ROUTINE_CHANGED"

    const val READING_SAVED = "READING_SAVED"
    const val READING_DELETED = "READING_DELETED"
    const val READING_UPDATED = "READING_UPDATED"
    const val READINGS_DELETE_ALL = "READINGS_DELETE_ALL"

    const val USER_CREATED = "USER_CREATED"
    const val USER_DELETED = "USER_DELETED"
}
