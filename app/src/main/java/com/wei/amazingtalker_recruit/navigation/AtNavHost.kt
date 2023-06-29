package com.wei.amazingtalker_recruit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.wei.amazingtalker_recruit.feature.login.login.navigation.loginScreen
import com.wei.amazingtalker_recruit.feature.login.welcome.navigation.welcomeGraph
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.navigation.scheduleRoute
import com.wei.amazingtalker_recruit.feature.teacherschedule.schedule.navigation.scheduleScreen
import com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail.navigation.scheduleDetailScreen
import com.wei.amazingtalker_recruit.ui.AtAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun AtNavHost(
    appState: AtAppState,
    modifier: Modifier = Modifier,
    startDestination: String = scheduleRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        scheduleScreen(
            navController = navController,
            tokenInvalidNavigate = { appState.tokenInvalidNavigate() },
            nestedGraphs = {
                scheduleDetailScreen(navController = navController)
            }
        )
        welcomeGraph(
            navController = navController,
            nestedGraphs = {
                loginScreen(onLoginClick = { appState.tokenValidNavigate() })
            }
        )

    }
}