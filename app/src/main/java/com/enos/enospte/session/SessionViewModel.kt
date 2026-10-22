package com.enos.enospte.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enos.enospte.data.repository.SessionRepository
import com.enos.enospte.domain.model.Session
import com.enos.enospte.domain.model.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SessionViewModel(private val repo: SessionRepository) : ViewModel() {

    val session: StateFlow<Session> =
        repo.session.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Session(isLoggedIn = false)
        )

    fun login(user: UserProfile) {
        viewModelScope.launch { repo.login(user) }
    }

    fun logout() {
        viewModelScope.launch { repo.logout() }
    }
}