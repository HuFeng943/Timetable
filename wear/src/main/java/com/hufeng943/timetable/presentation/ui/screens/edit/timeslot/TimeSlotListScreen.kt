package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hufeng943.timetable.presentation.ui.NavRoutes
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.presentation.ui.components.HandleEditUiState
import com.hufeng943.timetable.presentation.viewmodel.edit.timeslot.TimeSlotListViewModel

@Composable
fun TimeSlotListScreen(
    viewModel: TimeSlotListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val appConfig = LocalAppConfig.current

    HandleEditUiState(uiState) { data ->
        val firstDay = appConfig.effectiveFirstDayOfTheWeek
        val sortedTimeSlots = remember(data.timeSlots, firstDay) {
            data.timeSlots.sortedWith(
                compareBy<TimeSlotUi> { slot ->
                    slot.dayOfWeek?.let { (it.ordinal - firstDay.ordinal + 7) % 7 }
                        ?: Int.MAX_VALUE
                }.thenBy { slot ->
                    slot.startTime?.let { it.hour * 60 + it.minute }
                        ?: Int.MAX_VALUE
                }
            )
        }
        TimeSlotListPager(timeSlots = sortedTimeSlots, onAddTimeSlot = {
            navController.navigateSingle(NavRoutes.editTimeSlot(data.id))
        }, onTimeSlotClick = { timeSlotId ->
            navController.navigateSingle(NavRoutes.editTimeSlot(data.id, timeSlotId))
        })
    }
}