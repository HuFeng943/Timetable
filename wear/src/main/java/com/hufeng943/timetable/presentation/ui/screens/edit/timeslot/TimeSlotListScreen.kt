package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.edit.timeSlot.TimeSlotListViewModel

@Composable
fun TimeSlotListScreen(
    viewModel: TimeSlotListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current

    when (val state = uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Error -> ErrorScreen(state.throwable)
        is UiState.Empty -> ErrorScreen(AppError.UnexpectedEmpty())
        is UiState.Success -> {
            TimeSlotListPager(timeSlots = state.data.timeSlots, onAddTimeSlot = {
                navController.navigateSingle(NavRoutes.editTimeSlot(state.data.id))
            }, onTimeSlotClick = { timeSlotId ->
                navController.navigateSingle(NavRoutes.editTimeSlot(state.data.id, timeSlotId))
            })
        }
    }
}