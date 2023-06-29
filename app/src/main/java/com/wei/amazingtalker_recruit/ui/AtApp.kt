package com.wei.amazingtalker_recruit.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.wei.amazingtalker_recruit.core.designsystem.ui.component.AtAppSnackbar
import com.wei.amazingtalker_recruit.core.manager.ErrorTextPrefix
import com.wei.amazingtalker_recruit.core.manager.Message
import com.wei.amazingtalker_recruit.core.manager.SnackbarManager
import com.wei.amazingtalker_recruit.core.manager.SnackbarState
import com.wei.amazingtalker_recruit.core.utils.UiText
import com.wei.amazingtalker_recruit.navigation.AtNavHost


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
fun AtApp(
    windowSizeClass: WindowSizeClass,
    appState: AtAppState = rememberAtAppState(
        windowSizeClass = windowSizeClass,
    ),
    snackbarManager: SnackbarManager
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = snackbarHostState) {
        collectAndShowSnackbar(snackbarManager, snackbarHostState, context)
    }

    Scaffold(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.systemBarsPadding(),
                snackbar = { snackbarData ->
                    val isError = snackbarData.visuals.message.startsWith(ErrorTextPrefix)
                    AtAppSnackbar(snackbarData, isError)
                }
            )
        },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {

            Column(Modifier.fillMaxSize()) {
                // TODO: AtTopAppBar
                // AtTopAppBar()

                AtNavHost(appState = appState)
            }

            // TODO: We may want to add padding or spacer when the snackbar is shown so that
            //  content doesn't display behind it.
        }
    }
}

suspend fun collectAndShowSnackbar(
    snackbarManager: SnackbarManager,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    snackbarManager.messages.collect { messages ->
        if (messages.isNotEmpty()) {
            val message = messages.first()
            val text = getMessageText(message, context)

            if (message.state == SnackbarState.ERROR) {
                snackbarHostState.showSnackbar(
                    message = ErrorTextPrefix + text,
                    duration = SnackbarDuration.Long,
                )
            } else {
                snackbarHostState.showSnackbar(message = text)
            }
            snackbarManager.setMessageShown(message.id)
        }
    }
}

fun getMessageText(message: Message, context: Context): String {
    return when (message.uiText) {
        is UiText.DynamicString -> (message.uiText as UiText.DynamicString).value
        is UiText.StringResource -> context.getString(
            (message.uiText as UiText.StringResource).resId,
            *(message.uiText as UiText.StringResource).args.map { it.toString(context) }
                .toTypedArray()
        )
    }
}