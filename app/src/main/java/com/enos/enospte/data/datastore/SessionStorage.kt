package com.enos.enospte.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enos.enospte.domain.model.Session
import com.enos.enospte.domain.model.UserProfile
import com.enos.enospte.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "enos_pte")

class SessionStorage(private val context: Context) {

    private object Keys {
        val USER_ID = stringPreferencesKey("session_user_id")
        val USER_NAME = stringPreferencesKey("session_user_name")
        val USER_ROLE = stringPreferencesKey("session_user_role")
    }

    val sessionFlow: Flow<Session> = context.dataStore.data.map { prefs ->
        val id = prefs[Keys.USER_ID]
        val name = prefs[Keys.USER_NAME]
        val role = prefs[Keys.USER_ROLE]?.let { runCatching { UserRole.valueOf(it) }.getOrNull() }

        Session(
            isLoggedIn = id != null,
            userId = id,
            userName = name,
            role = role
        )
    }

    suspend fun saveLogin(user: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = user.id
            prefs[Keys.USER_NAME] = user.displayName
            prefs[Keys.USER_ROLE] = user.role.name
        }
    }

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.USER_ID)
            prefs.remove(Keys.USER_NAME)
            prefs.remove(Keys.USER_ROLE)
        }
    }
}