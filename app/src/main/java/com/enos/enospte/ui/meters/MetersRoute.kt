package com.enos.enospte.ui.meters

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.enos.enospte.core.navigation.Routes
import com.enos.enospte.data.source.MeterCatalog
import com.enos.enospte.domain.model.Session
import com.enos.enospte.domain.model.UserRole
import com.enos.enospte.session.SessionViewModel
import com.enos.enospte.viewmodel.MainViewModel

fun NavGraphBuilder.metersGraph(
    navController: NavController,
    sessionProvider: () -> Session,
    sessionVm: SessionViewModel,
    mainVm: MainViewModel
) {
    composable(Routes.METER_ENTRY) {
        val session = sessionProvider()
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

        MeterEntryScreen(
            userId = userId,
            enteredBy = enteredBy,
            isAdmin = false,
            onLogout = { sessionVm.logout() },
            onAdminHistory = {
                navController.navigate(Routes.ADMIN_HISTORY)
            },
            onOpenSettings = {
                navController.navigate(Routes.SETTINGS)
            },
            vm = mainVm
        )
    }

    composable(
        route = Routes.METER_ORDER,
        arguments = listOf(
            navArgument(Routes.METER_ORDER_ARG_ROUTINE_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val session = sessionProvider()
        val userId = session.userId

        if (
            !session.isLoggedIn ||
            session.role != UserRole.USER ||
            userId.isNullOrBlank()
        ) {
            LaunchedEffect(Unit) {
                sessionVm.logout()
            }
            return@composable
        }

        val routineId = backStackEntry.arguments
            ?.getString(Routes.METER_ORDER_ARG_ROUTINE_ID)
            ?.takeIf { it.isNotBlank() }
            ?: return@composable

        MeterOrderScreen(
            userId = userId,
            routineId = routineId,
            meters = MeterCatalog.getMeters(),
            onBack = { navController.popBackStack() }
        )
    }
}
