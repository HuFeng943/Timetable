package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.runtime.Composable
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState

@Composable
fun <T> HandleEditUiState(
    uiState: UiState<T>,
    content: @Composable (T) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(uiState.throwable)
        is UiState.Empty -> ErrorScreen(AppError.UnexpectedEmpty())
        is UiState.Success -> content(uiState.data)
    }
}