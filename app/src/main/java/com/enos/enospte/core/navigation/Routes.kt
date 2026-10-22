package com.enos.enospte.core.navigation

object Routes {
    const val LOGIN = "login"
    const val METER_ENTRY = "meter_entry"

    const val ADMIN_HOME = "admin_home"
    const val ADMIN_USERS = "admin_users"
    const val ADMIN_HISTORY = "admin_history"

    const val SETTINGS = "settings"
    const val ADMIN_SETTINGS = "admin_settings"

    const val METER_ORDER_ARG_ROUTINE_ID = "routineId"
    const val METER_ORDER = "meter_order/{$METER_ORDER_ARG_ROUTINE_ID}"

    fun meterOrder(routineId: String): String = "meter_order/$routineId"
}
