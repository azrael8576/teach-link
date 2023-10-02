package com.wei.amazingtalker.feature.login.welcome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wei.amazingtalker.feature.login.welcome.WelcomeRoute
import timber.log.Timber

const val welcomeRoute = "welcome_route"

fun NavController.navigateToWelcome(navOptions: NavOptions? = null) {
    Timber.d("navigateToWelcome")
    this.navigate(welcomeRoute, navOptions)
}

fun NavGraphBuilder.welcomeGraph(
    isCompact: Boolean,
    navController: NavHostController,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(route = welcomeRoute) {
        WelcomeRoute(
            isCompact = isCompact,
            navController = navController
        )
    }
    nestedGraphs()
}