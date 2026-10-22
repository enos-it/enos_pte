package com.enos.enospte.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enos.enospte.data.datastore.UserPrefsStorage
import com.enos.enospte.data.db.DatabaseProvider
import com.enos.enospte.data.db.ActivityLogEntity
import com.enos.enospte.data.model.ActivityEventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = UserPrefsStorage(app.applicationContext)
    private val activityDao = DatabaseProvider.get(app).activityLogDao()

    fun observePin(userId: String): Flow<String?> = prefs.observePin(userId)

    fun setPin(userId: String, newPin: String) {
        viewModelScope.launch {
            prefs.setPin(userId, newPin)

            activityDao.insert(
                ActivityLogEntity(
                    id = UUID.randomUUID().toString(),
                    timestampMs = System.currentTimeMillis(),
                    userId = userId,
                    userName = userId,
                    type = ActivityEventType.PIN_CHANGED,
                    message = "PIN changed"
                )
            )
        }
    }
}