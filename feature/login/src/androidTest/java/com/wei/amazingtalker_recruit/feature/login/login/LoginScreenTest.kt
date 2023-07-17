package com.wei.amazingtalker_recruit.feature.login.login

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import org.junit.Rule
import org.junit.Test
import kotlin.properties.ReadOnlyProperty

/**
 * UI tests for [LoginScreen] composable.
 *
 * 遵循此模型，找到測試使用者介面元素、檢查其屬性、和透過測試規則執行動作：
 * composeTestRule{.finder}{.assertion}{.action}
 *
 * Testing cheatsheet：
 * https://developer.android.com/jetpack/compose/testing-cheatsheet
 */
class LoginScreenTest {

    /**
     * 通常我們使用 createComposeRule()，作為 composeTestRule
     *
     * 但若測試案例需查找資源檔 e.g. R.string.welcome。
     * 使用 createAndroidComposeRule()，作為 composeTestRule
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
        ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val loginTitle by composeTestRule.stringResource(R.string.login_title)
    private val account by composeTestRule.stringResource(R.string.account)
    private val password by composeTestRule.stringResource(R.string.password)
    private val login by composeTestRule.stringResource(R.string.login)

    @Test
    fun showsLoginFrom() {
        // 設置 Compose UI
        composeTestRule.setContent {
            AtTheme {
                LoginScreen(
                    login = { _, _ -> }
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")

        composeTestRule.onNodeWithContentDescription(loginTitle).assertExists()
        composeTestRule.onNodeWithContentDescription(account).assertExists()
        composeTestRule.onNodeWithContentDescription(password).assertExists()
        composeTestRule.onNodeWithContentDescription(login).assertExists()
    }

}