package com.enos.enospte.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enos.enospte.data.repository.SessionRepository
import com.enos.enospte.data.datastore.SessionStorage

class SessionViewModelFactory(private val appContext: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val storage = SessionStorage(appContext)
        val repo = SessionRepository(storage)
        return SessionViewModel(repo) as T
    }
}