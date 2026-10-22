package com.enos.enospte.domain.model

enum class UserRole { USER, ADMIN }

data class UserProfile(
    val id: String,
    val displayName: String,
    val role: UserRole = UserRole.USER
)

data class Session(
    val isLoggedIn: Boolean,
    val userId: String? = null,
    val userName: String? = null,
    val role: UserRole? = null
) {
    val enteredBy: String? get() = userName ?: userId
}