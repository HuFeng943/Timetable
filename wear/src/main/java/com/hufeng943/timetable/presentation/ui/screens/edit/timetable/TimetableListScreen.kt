package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.presentation.ui.NavRoutes.editTimetable
import com.hufeng943.timetable.presentation.ui.NavRoutes.listCourse
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.edit.timetable.TimetableListViewModel

@Composable
fun TimetableListScreen(
    viewModel: TimetableListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current

    when (val state = uiState) {
        UiState.Loading -> LoadingScreen()
        else -> {
            val data = (state as? UiState.Success)?.data ?: emptyList()
            TimetableListPager(
                timetables = data,
                onAddTimetable = {
                    navController.navigate(NavRoutes.EDIT_TIMETABLE)
                },
                onTimetableClick = { id ->
                    navController.navigate(listCourse(id))
                },
                onTimetableLongClick = { id ->
                    navController.navigate(editTimetable(id))
                }
            )
        }
    }
}