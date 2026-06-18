package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.MaterialTheme
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun <T> HandleEditUiState(
    uiState: UiState<T>,
    emptyContent: @Composable () -> Unit = { ErrorScreen(AppError.UnexpectedEmpty()) },
    successContent: @Composable (T) -> Unit
) {
    val delayDuration = 140.milliseconds
    var showLoadingActual by remember { mutableStateOf(false) }

    // 监听 uiState 的变化
    LaunchedEffect(uiState) {
        if (uiState is UiState.Loading) {
            delay(delayDuration)
            showLoadingActual = true
        } else {
            showLoadingActual = false
        }
    }

    AnimatedContent(
        targetState = uiState, transitionSpec = {
            if (targetState is UiState.Loading || initialState is UiState.Loading) {
                // 淡入 + 从 92% 放大到 100%
                (fadeIn(animationSpec = tween(220)) + scaleIn(
                    initialScale = 0.92f, animationSpec = tween(220)
                ))
                    // 淡出 + 缩小到 92%
                    .togetherWith(
                        fadeOut(animationSpec = tween(180)) + scaleOut(
                            targetScale = 0.92f, animationSpec = tween(180)
                        )
                    )
            } else {
                fadeIn(tween(0)) togetherWith fadeOut(tween(0))
            }
        }, label = "UiStateTransition"
    ) { targetState ->
        when (targetState) {
            is UiState.Loading -> {
                if (showLoadingActual) {
                    LoadingScreen()
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }

            is UiState.Error -> ErrorScreen(targetState.throwable)
            is UiState.Empty -> emptyContent()
            is UiState.Success -> successContent(targetState.data)
        }
    }
}