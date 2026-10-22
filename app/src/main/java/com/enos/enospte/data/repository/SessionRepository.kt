package com.enos.enospte.data.repository

import com.enos.enospte.data.datastore.SessionStorage
import com.enos.enospte.domain.model.Session
import com.enos.enospte.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val storage: SessionStorage) {
    val session: Flow<Session> = storage.sessionFlow
    suspend fun login(user: UserProfile) = storage.saveLogin(user)
    suspend fun logout() = storage.clear()
}