package com.wei.amazingtalker.ui

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import androidx.navigation.testing.TestNavHostController
import com.google.common.truth.Truth.assertThat
import com.wei.amazingtalker.core.designsystem.ui.AtNavigationType
import com.wei.amazingtalker.core.testing.util.TestNetworkMonitor
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/**
 * Tests [AtAppState].
 *
 * Note: This could become an unit test if Robolectric is added to the project and the Context
 * is faked.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class AtAppStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Create the test dependencies.
    private val networkMonitor = TestNetworkMonitor()

    // Subject under test.
    private lateinit var state: AtAppState

    @Test
    fun verifyCurrentDestinationIsSet_whenNavigatedToDestination() = runTest {
        var currentDestination: String? = null

        composeTestRule.setContent {
            val navController = rememberTestNavController()
            state = remember(navController) {
                AtAppState(
                    navController = navController,
                    coroutineScope = backgroundScope,
                    windowSizeClass = getCompactWindowClass(),
                    networkMonitor = networkMonitor,
                    displayFeatures = emptyList(),
                )
            }

            // Update currentDestination whenever it changes
            currentDestination = state.currentDestination?.route

            // Navigate to destination b once
            LaunchedEffect(Unit) {
                navController.setCurrentDestination("b")
            }
        }

        assertThat("b").isEqualTo(currentDestination)
    }

    @Test
    fun verifyTopLevelDestinationsContainExpectedNames() = runTest {
        composeTestRule.setContent {
            state = rememberAtAppState(
                windowSizeClass = getCompactWindowClass(),
                networkMonitor = networkMonitor,
                displayFeatures = emptyList(),
            )
        }

        assertThat(state.topLevelDestinations).hasSize(3)
        assertThat(state.topLevelDestinations[0].name).ignoringCase().contains("schedule")
        assertThat(state.topLevelDestinations[1].name).ignoringCase().contains("home")
        assertThat(state.topLevelDestinations[2].name).ignoringCase().contains("contact_me")
    }

    @Test
    fun verifyBottomNavigationDisplayed_whenWindowSizeIsCompact() = runTest {
        composeTestRule.setContent {
            state = AtAppState(
                navController = NavHostController(LocalContext.current),
                coroutineScope = backgroundScope,
                windowSizeClass = getCompactWindowClass(),
                networkMonitor = networkMonitor,
                displayFeatures = emptyList(),
            )
            assertThat(state.navigationType).isEqualTo(AtNavigationType.BOTTOM_NAVIGATION)
        }
    }

    @Test
    fun verifyNavigationRailDisplayed_whenWindowSizeIsMedium() = runTest {
        composeTestRule.setContent {
            state = AtAppState(
                navController = NavHostController(LocalContext.current),
                coroutineScope = backgroundScope,
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(800.dp, 800.dp)),
                networkMonitor = networkMonitor,
                displayFeatures = emptyList(),
            )
            assertThat(state.navigationType).isEqualTo(AtNavigationType.NAVIGATION_RAIL)
        }
    }

    @Test
    fun verifyPermanentNavDrawerDisplayed_whenWindowSizeIsExpanded() = runTest {
        composeTestRule.setContent {
            state = AtAppState(
                navController = NavHostController(LocalContext.current),
                coroutineScope = backgroundScope,
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 1200.dp)),
                networkMonitor = networkMonitor,
                displayFeatures = emptyList(),
            )
            assertThat(state.navigationType).isEqualTo(AtNavigationType.PERMANENT_NAVIGATION_DRAWER)
        }
    }

    @Test
    fun verifyStateIsOffline_whenNetworkMonitorReportsDisconnection() = runTest(UnconfinedTestDispatcher()) {
        val results = mutableListOf<Boolean>()

        composeTestRule.setContent {
            state = AtAppState(
                navController = NavHostController(LocalContext.current),
                coroutineScope = backgroundScope,
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 1200.dp)),
                networkMonitor = networkMonitor,
                displayFeatures = emptyList(),
            )
        }

        backgroundScope.launch {
            state.isOffline.collect { value ->
                results.add(value)
            }
        }

        networkMonitor.setConnected(false)
        assertTrue(results.contains(true))
    }

    private fun getCompactWindowClass() = WindowSizeClass.calculateFromSize(DpSize(500.dp, 300.dp))
}

@Composable
private fun rememberTestNavController(): TestNavHostController {
    val context = LocalContext.current
    return remember<TestNavHostController> {
        TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            graph = createGraph(startDestination = "a") {
                composable("a") { }
                composable("b") { }
                composable("c") { }
            }
        }
    }
}