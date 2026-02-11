package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hufeng943.timetable.presentation.ui.screens.loading.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.home.TimetableViewModel

@Composable
fun TimetableListScreen(
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val uiState by viewModel.allTimetables.collectAsState()
    when (val state = uiState) {
        UiState.Loading -> LoadingScreen()
        else -> {
            val data = (state as? UiState.Success)?.data ?: emptyList()
            TimetableListPager(data)
        }
    }
}