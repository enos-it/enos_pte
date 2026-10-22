package com.enos.enospte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enos.enospte.session.SessionViewModel
import com.enos.enospte.session.SessionViewModelFactory
import com.enos.enospte.ui.navigation.AppNav
import com.enos.enospte.ui.theme.EnosPTETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EnosPTETheme {
                val sessionVm: SessionViewModel = viewModel(
                    factory = SessionViewModelFactory(applicationContext)
                )

                AppNav(sessionVm = sessionVm)
            }
        }
    }
}