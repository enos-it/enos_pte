package com.enos.enospte.data.source

import com.enos.enospte.domain.model.UserProfile
import com.enos.enospte.domain.model.UserRole

object UserCatalog {

    fun getUsers(): List<UserProfile> = listOf(
        UserProfile(id = "JURE", displayName = "Jure", role = UserRole.USER),
        UserProfile(id = "LUKA", displayName = "Luka", role = UserRole.USER),
        UserProfile(id = "JAN", displayName = "Jan", role = UserRole.USER),
        UserProfile(id = "TOMAZ", displayName = "Tomaž", role = UserRole.USER),
        UserProfile(id = "JANEZ", displayName = "Janez", role = UserRole.USER),
        UserProfile(id = "DARKO", displayName = "Darko", role = UserRole.USER),
        UserProfile(id = "ADMIN", displayName = "Admin", role = UserRole.ADMIN)
    )

    fun defaultPin(userId: String): String? = when (userId.uppercase()) {
        "JURE" -> "1111"
        "LUKA" -> "2222"
        "JAN" -> "3333"
        "TOMAZ" -> "4444"
        "JANEZ" -> "5555"
        "DARKO" -> "6666"
        "ADMIN" -> "0000"
        else -> null
    }
}
