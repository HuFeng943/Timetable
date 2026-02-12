package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.edit.course.CourseListViewModel

@Composable
fun CourseListScreen(
    viewModel: CourseListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    when (val state = uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(state.throwable)
        is UiState.Empty -> ErrorScreen(AppError.UnexpectedEmpty())
        is UiState.Success -> CourseListPager(state.data)
    }
}