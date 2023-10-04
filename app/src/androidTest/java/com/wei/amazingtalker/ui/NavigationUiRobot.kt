import android.graphics.Rect
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.DpSize
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.window.layout.FoldingFeature
import com.google.accompanist.testharness.TestHarness
import com.wei.amazingtalker.R
import com.wei.amazingtalker.core.data.utils.NetworkMonitor
import com.wei.amazingtalker.core.manager.SnackbarManager
import com.wei.amazingtalker.ui.AtApp
import com.wei.amazingtalker.uitesthiltmanifest.HiltComponentActivity
import kotlin.properties.ReadOnlyProperty

/**
 * Robot for [NavigationUiTest].
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
internal fun navigationUiRobot(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltComponentActivity>, HiltComponentActivity>,
    func: NavigationUiRobot.() -> Unit,
) = NavigationUiRobot(composeTestRule).apply(func)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal open class NavigationUiRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltComponentActivity>, HiltComponentActivity>,
) {
    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val atBottomBarTag by composeTestRule.stringResource(R.string.tag_at_bottom_bar)
    private val atNavRailTag by composeTestRule.stringResource(R.string.tag_at_nav_rail)
    private val atNavDrawerTag by composeTestRule.stringResource(R.string.tag_at_nav_drawer)

    private val atBottomBar by lazy {
        composeTestRule.onNodeWithTag(
            atBottomBarTag,
            useUnmergedTree = true,
        )
    }

    private val atNavRail by lazy {
        composeTestRule.onNodeWithTag(
            atNavRailTag,
            useUnmergedTree = true,
        )
    }
    private val atNavDrawer by lazy {
        composeTestRule.onNodeWithTag(
            atNavDrawerTag,
            useUnmergedTree = true,
        )
    }

    fun setAtAppContent(
        dpSize: DpSize,
        networkMonitor: NetworkMonitor,
        snackbarManager: SnackbarManager,
    ) {
        composeTestRule.setContent {
            TestHarness(dpSize) {
                BoxWithConstraints {
                    AtApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            dpSize,
                        ),
                        networkMonitor = networkMonitor,
                        displayFeatures = emptyList(),
                        snackbarManager = snackbarManager,
                    )
                }
            }
        }
    }

    fun setAtAppContentWithBookPosture(
        dpSize: DpSize,
        networkMonitor: NetworkMonitor,
        snackbarManager: SnackbarManager,
    ) {
        composeTestRule.setContent {
            TestHarness(dpSize) {
                BoxWithConstraints {
                    val bookFoldBounds = getBookFoldBounds(dpSize)
                    val bookFoldingFeature = getBookFoldingFeature(bookFoldBounds)

                    AtApp(
                        windowSizeClass = WindowSizeClass.calculateFromSize(
                            dpSize,
                        ),
                        networkMonitor = networkMonitor,
                        displayFeatures = listOf(bookFoldingFeature),
                        snackbarManager = snackbarManager,
                    )
                }
            }
        }
    }

    private fun getBookFoldBounds(dpSize: DpSize): Rect {
        val middleHeight = (dpSize.height / 2f).value.toInt()
        return Rect(
            0,
            middleHeight,
            dpSize.width.value.toInt(),
            middleHeight,
        )
    }

    private fun getBookFoldingFeature(foldBounds: Rect): FoldingFeature {
        return object : FoldingFeature {
            override val bounds: Rect = foldBounds
            override val isSeparating: Boolean = true
            override val occlusionType: FoldingFeature.OcclusionType = FoldingFeature.OcclusionType.NONE
            override val orientation: FoldingFeature.Orientation = FoldingFeature.Orientation.VERTICAL
            override val state: FoldingFeature.State = FoldingFeature.State.HALF_OPENED
        }
    }

    fun verifyAtBottomBarDisplayed() {
        atBottomBar.assertExists().assertIsDisplayed()
    }

    fun verifyAtNavRailDisplayed() {
        atNavRail.assertExists().assertIsDisplayed()
    }

    fun verifyAtNavDrawerDisplayed() {
        atNavDrawer.assertExists().assertIsDisplayed()
    }

    fun verifyAtBottomBarDoesNotExist() {
        atBottomBar.assertDoesNotExist()
    }

    fun verifyAtNavRailDoesNotExist() {
        atNavRail.assertDoesNotExist()
    }

    fun verifyAtNavDrawerDoesNotExist() {
        atNavDrawer.assertDoesNotExist()
    }
}
