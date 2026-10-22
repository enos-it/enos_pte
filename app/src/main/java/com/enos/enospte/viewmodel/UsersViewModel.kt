package com.enos.enospte.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enos.enospte.data.datastore.UserPrefsStorage
import com.enos.enospte.data.datastore.UserStorage
import com.enos.enospte.data.db.ActivityLogEntity
import com.enos.enospte.data.db.DatabaseProvider
import com.enos.enospte.data.source.UserCatalog
import com.enos.enospte.data.model.ActivityEventType
import com.enos.enospte.domain.model.UserProfile
import com.enos.enospte.domain.model.UserRole
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class UsersViewModel(app: Application) : AndroidViewModel(app) {

    private val userStorage = UserStorage(app.applicationContext)
    private val pinStorage = UserPrefsStorage(app.applicationContext)
    private val activityDao = DatabaseProvider.get(app).activityLogDao()

    val users: StateFlow<List<UserProfile>> =
        userStorage.usersFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserCatalog.getUsers()
        )

    init {
        viewModelScope.launch {
            userStorage.ensureSeeded()
        }
    }

    fun addUser(
        id: String,
        displayName: String,
        pin: String,
        actorId: String,
        actorName: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            val cleanId = id.trim().uppercase()
            val cleanName = displayName.trim()

            val error = when {
                cleanId.isBlank() -> "Vpiši ID uporabnika."
                !cleanId.matches(Regex("[A-Z0-9_\\-]+")) ->
                    "ID lahko vsebuje velike črke, številke, _ in -."
                cleanName.isBlank() -> "Vpiši ime uporabnika."
                pin.length != 4 || pin.any { !it.isDigit() } ->
                    "PIN mora imeti 4 številke."
                users.value.any { it.id.equals(cleanId, ignoreCase = true) } ->
                    "Uporabnik s tem ID-jem že obstaja."
                users.value.any { it.displayName.equals(cleanName, ignoreCase = true) } ->
                    "Uporabnik s tem imenom že obstaja."
                else -> null
            }

            if (error != null) {
                onResult(error)
                return@launch
            }

            val user = UserProfile(
                id = cleanId,
                displayName = cleanName,
                role = UserRole.USER
            )

            userStorage.addUser(user)
            pinStorage.setPin(user.id, pin)

            logEvent(
                actorId = actorId,
                actorName = actorName,
                type = ActivityEventType.USER_CREATED,
                message = "Created user ${user.id}",
                details = """{"targetUserId":"${user.id}","targetUserName":"${user.displayName}"}"""
            )

            onResult(null)
        }
    }

    fun deleteUser(
        user: UserProfile,
        actorId: String,
        actorName: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            val error = when {
                user.role == UserRole.ADMIN || user.id.equals("ADMIN", ignoreCase = true) ->
                    "Admin uporabnika ni mogoče izbrisati."
                user.id.equals(actorId, ignoreCase = true) ->
                    "Prijavljenega uporabnika ni mogoče izbrisati."
                else -> null
            }

            if (error != null) {
                onResult(error)
                return@launch
            }

            userStorage.deleteUser(user.id)
            pinStorage.removePin(user.id)

            logEvent(
                actorId = actorId,
                actorName = actorName,
                type = ActivityEventType.USER_DELETED,
                message = "Deleted user ${user.id}",
                details = """{"targetUserId":"${user.id}","targetUserName":"${user.displayName}"}"""
            )

            onResult(null)
        }
    }

    fun resetPin(
        user: UserProfile,
        newPin: String,
        actorId: String,
        actorName: String,
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {
            if (newPin.length != 4 || newPin.any { !it.isDigit() }) {
                onResult("PIN mora imeti 4 številke.")
                return@launch
            }

            pinStorage.setPin(user.id, newPin)

            logEvent(
                actorId = actorId,
                actorName = actorName,
                type = ActivityEventType.PIN_RESET,
                message = "Reset PIN for ${user.id}",
                details = """{"targetUserId":"${user.id}","targetUserName":"${user.displayName}"}"""
            )

            onResult(null)
        }
    }

    private suspend fun logEvent(
        actorId: String,
        actorName: String,
        type: String,
        message: String,
        details: String?
    ) {
        activityDao.insert(
            ActivityLogEntity(
                id = UUID.randomUUID().toString(),
                timestampMs = System.currentTimeMillis(),
                userId = actorId,
                userName = actorName,
                type = type,
                message = message,
                detailsJson = details
            )
        )
    }
}
