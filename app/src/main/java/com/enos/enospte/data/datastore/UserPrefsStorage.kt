package com.enos.enospte.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userPrefsDataStore by preferencesDataStore(name = "user_prefs")

class UserPrefsStorage(private val context: Context) {

    private fun pinKey(userId: String) =
        stringPreferencesKey("pin_${userId.uppercase()}")

    fun observePin(userId: String): Flow<String?> {
        val key = pinKey(userId)
        return context.userPrefsDataStore.data.map { prefs -> prefs[key] }
    }

    suspend fun setPin(userId: String, newPin: String) {
        val key = pinKey(userId)

        context.userPrefsDataStore.edit { prefs ->
            prefs[key] = newPin
        }
    }

    suspend fun removePin(userId: String) {
        val key = pinKey(userId)

        context.userPrefsDataStore.edit { prefs ->
            prefs.remove(key)
        }
    }
}
