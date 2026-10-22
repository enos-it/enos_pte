package com.enos.enospte.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enos.enospte.core.navigation.Routes
import com.enos.enospte.domain.model.UserRole
import com.enos.enospte.session.SessionViewModel
import com.enos.enospte.ui.admin.AdminHistoryRoute
import com.enos.enospte.ui.admin.AdminHomeScreen
import com.enos.enospte.ui.admin.AdminUsersScreen
import com.enos.enospte.ui.login.LoginScreen
import com.enos.enospte.ui.meters.metersGraph
import com.enos.enospte.ui.settings.SettingsScreen
import com.enos.enospte.viewmodel.MainViewModel

@Composable
fun AppNav(sessionVm: SessionViewModel) {
    val nav = rememberNavController()
    val session by sessionVm.session.collectAsState()
    val latestSession = rememberUpdatedState(session)
    val mainVm: MainViewModel = viewModel()

    LaunchedEffect(session.isLoggedIn, session.role) {
        val target = when {
            !session.isLoggedIn -> Routes.LOGIN
            session.role == UserRole.ADMIN -> Routes.ADMIN_HOME
            else -> Routes.METER_ENTRY
        }

        val current = nav.currentBackStackEntry?.destination?.route

        if (current != target) {
            nav.navigate(target) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = nav,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { userProfile ->
                    sessionVm.login(userProfile)
                }
            )
        }

        metersGraph(
            navController = nav,
            sessionProvider = { latestSession.value },
            sessionVm = sessionVm,
            mainVm = mainVm
        )

        composable(Routes.ADMIN_HOME) {
            val userId = session.userId
            val enteredBy = session.enteredBy

            if (
                !session.isLoggedIn ||
                session.role != UserRole.ADMIN ||
                userId.isNullOrBlank() ||
                enteredBy.isNullOrBlank()
            ) {
                LaunchedEffect(Unit) {
                    sessionVm.logout()
                }
                return@composable
            }

            AdminHomeScreen(
                enteredBy = enteredBy,
                onUsers = { nav.navigate(Routes.ADMIN_USERS) },
                onHistory = { nav.navigate(Routes.ADMIN_HISTORY) },
                onChangePin = { nav.navigate(Routes.ADMIN_SETTINGS) },
                onLogout = { sessionVm.logout() }
            )
        }

        composable(Routes.ADMIN_USERS) {
            val userId = session.userId
            val enteredBy = session.enteredBy

            if (
                !session.isLoggedIn ||
                session.role != UserRole.ADMIN ||
                userId.isNullOrBlank() ||
                enteredBy.isNullOrBlank()
            ) {
                LaunchedEffect(Unit) {
                    sessionVm.logout()
                }
                return@composable
            }

            AdminUsersScreen(
                currentUserId = userId,
                currentUserName = enteredBy,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.ADMIN_HISTORY) {
            if (!session.isLoggedIn) {
                LaunchedEffect(Unit) {
                    sessionVm.logout()
                }
                return@composable
            }

            AdminHistoryRoute(
                vm = mainVm,
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            val userId = session.userId
            val enteredBy = session.enteredBy

            if (
                !session.isLoggedIn ||
                session.role != UserRole.USER ||
                userId.isNullOrBlank() ||
                enteredBy.isNullOrBlank()
            ) {
                LaunchedEffect(Unit) {
                    sessionVm.logout()
                }
                return@composable
            }

            SettingsScreen(
                userId = userId,
                enteredBy = enteredBy,
                isAdmin = false,
                showRoutineSettings = true,
                onBack = { nav.popBackStack() },
                onMeterOrder = { routineId ->
                    nav.navigate(Routes.meterOrder(routineId))
                }
            )
        }

        composable(Routes.ADMIN_SETTINGS) {
            val userId = session.userId
            val enteredBy = session.enteredBy

            if (
                !session.isLoggedIn ||
                session.role != UserRole.ADMIN ||
                userId.isNullOrBlank() ||
                enteredBy.isNullOrBlank()
            ) {
                LaunchedEffect(Unit) {
                    sessionVm.logout()
                }
                return@composable
            }

            SettingsScreen(
                userId = userId,
                enteredBy = enteredBy,
                isAdmin = true,
                showRoutineSettings = false,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
