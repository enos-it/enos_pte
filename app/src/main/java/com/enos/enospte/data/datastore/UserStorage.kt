package com.enos.enospte.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enos.enospte.data.source.UserCatalog
import com.enos.enospte.domain.model.UserProfile
import com.enos.enospte.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.usersDataStore by preferencesDataStore(name = "users")

class UserStorage(private val context: Context) {

    private object Keys {
        val USERS_JSON = stringPreferencesKey("users_json")
    }

    val usersFlow: Flow<List<UserProfile>> =
        context.usersDataStore.data.map { prefs ->
            decodeUsers(prefs[Keys.USERS_JSON])
                .ifEmpty { UserCatalog.getUsers() }
        }

    suspend fun ensureSeeded() {
        context.usersDataStore.edit { prefs ->
            if (decodeUsers(prefs[Keys.USERS_JSON]).isEmpty()) {
                prefs[Keys.USERS_JSON] = encodeUsers(UserCatalog.getUsers())
            }
        }
    }

    suspend fun addUser(user: UserProfile) {
        context.usersDataStore.edit { prefs ->
            val current = decodeUsers(prefs[Keys.USERS_JSON])
                .ifEmpty { UserCatalog.getUsers() }

            prefs[Keys.USERS_JSON] = encodeUsers(current + user)
        }
    }

    suspend fun deleteUser(userId: String) {
        context.usersDataStore.edit { prefs ->
            val current = decodeUsers(prefs[Keys.USERS_JSON])
                .ifEmpty { UserCatalog.getUsers() }

            prefs[Keys.USERS_JSON] = encodeUsers(
                current.filterNot { it.id.equals(userId, ignoreCase = true) }
            )
        }
    }

    private fun encodeUsers(users: List<UserProfile>): String {
        val array = JSONArray()

        users.forEach { user ->
            array.put(
                JSONObject()
                    .put("id", user.id)
                    .put("displayName", user.displayName)
                    .put("role", user.role.name)
            )
        }

        return array.toString()
    }

    private fun decodeUsers(raw: String?): List<UserProfile> {
        if (raw.isNullOrBlank()) return emptyList()

        return runCatching {
            val array = JSONArray(raw)

            buildList {
                for (index in 0 until array.length()) {
                    val item = array.getJSONObject(index)
                    val id = item.optString("id").trim()
                    val displayName = item.optString("displayName").trim()

                    if (id.isBlank() || displayName.isBlank()) continue

                    val role = runCatching {
                        UserRole.valueOf(item.optString("role"))
                    }.getOrDefault(UserRole.USER)

                    add(
                        UserProfile(
                            id = id,
                            displayName = displayName,
                            role = role
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
