package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material3.TimePicker
import androidx.wear.compose.material3.TimePickerType
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.components.HandleEditUiState
import com.hufeng943.timetable.presentation.ui.screens.common.DayOfWeekSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.common.DeleteConfirmScreen
import com.hufeng943.timetable.presentation.ui.screens.common.NameEditScreen
import com.hufeng943.timetable.presentation.ui.screens.common.RecurrenceSelectionScreen
import com.hufeng943.timetable.presentation.ui.screens.edit.InternalNavRoutes
import com.hufeng943.timetable.presentation.viewmodel.edit.timeslot.EditTimeSlotAction
import com.hufeng943.timetable.presentation.viewmodel.edit.timeslot.EditTimeSlotViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun EditTimeSlotScreen(
    viewModel: EditTimeSlotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current
    val internalNavController = rememberSwipeDismissableNavController()
    val config = LocalAppConfig.current


    SwipeDismissableNavHost(
        navController = internalNavController, startDestination = InternalNavRoutes.MAIN
    ) {
        composable(InternalNavRoutes.MAIN) {
            HandleEditUiState(uiState) { timeSlot ->
                Log.d("timeSlot", "timeSlot: $timeSlot")
                EditTimeSlotMainPager(timeSlot = timeSlot, onSave = {
                    viewModel.onAction(EditTimeSlotAction.Upsert)
                    navController.popBackStack()
                }, onStartTimeClick = {
                    internalNavController.navigateSingle(InternalNavRoutes.START_TIME)
                }, onEndTimeClick = {
                    internalNavController.navigateSingle(InternalNavRoutes.END_TIME)
                }, onDayOfWeekClick = {
                    internalNavController.navigateSingle(InternalNavRoutes.WEEK_DAY)
                }, onRecurrenceClick = {
                    internalNavController.navigateSingle(InternalNavRoutes.RECURRENCE)
                }, onRemarkClick = {
                    internalNavController.navigateSingle(InternalNavRoutes.NAME)
                }, onRemarkLongClick = {
                    viewModel.onAction(EditTimeSlotAction.UpdateRemark(null))
                }, onDelete = {
                    internalNavController.navigateSingle(InternalNavRoutes.DELETE_CONFIRM)
                })
            }
        }

        composable(InternalNavRoutes.START_TIME) {
            HandleEditUiState(uiState) { timeSlot ->
                TimePicker(
                    initialTime = (timeSlot.startTime ?: Clock.System.now().toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ).time).toJavaLocalTime(), onTimePicked = { newTime ->
                        viewModel.onAction(EditTimeSlotAction.UpdateStartTime(newTime.toKotlinLocalTime()))
                        internalNavController.popBackStack()
                    }, timePickerType = if (config.is24HourFormat) {
                        TimePickerType.HoursMinutes24H
                    } else {
                        TimePickerType.HoursMinutesAmPm12H
                    }
                )
            }
        }

        composable(InternalNavRoutes.END_TIME) {
            HandleEditUiState(uiState) { timeSlot ->
                TimePicker(
                    initialTime = (timeSlot.endTime ?: Clock.System.now().toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ).time).toJavaLocalTime(), onTimePicked = { newTime ->
                        viewModel.onAction(EditTimeSlotAction.UpdateEndTime(newTime.toKotlinLocalTime()))
                        internalNavController.popBackStack()
                    }, timePickerType = if (config.is24HourFormat) {
                        TimePickerType.HoursMinutes24H
                    } else {
                        TimePickerType.HoursMinutesAmPm12H
                    }
                )
            }
        }

        composable(InternalNavRoutes.WEEK_DAY) {
            HandleEditUiState(uiState) { timeSlot ->
                DayOfWeekSelectionScreen(
                    initialDay = timeSlot.dayOfWeek, onDaySelected = { day ->
                        viewModel.onAction(EditTimeSlotAction.UpdateDayOfWeek(day))
                    })
            }
        }

        composable(InternalNavRoutes.RECURRENCE) {
            HandleEditUiState(uiState) { timeSlot ->
                RecurrenceSelectionScreen(
                    initialPattern = timeSlot.recurrence, onPatternSelected = { pattern ->
                        viewModel.onAction(EditTimeSlotAction.UpdateRecurrence(pattern))
                    })
            }
        }

        composable(InternalNavRoutes.NAME) {
            HandleEditUiState(uiState) { timeSlot ->
                NameEditScreen(
                    label = stringResource(R.string.edit_timeslot_remark_hint),
                    initialText = timeSlot.remark ?: ""
                ) { newRemark ->
                    viewModel.onAction(EditTimeSlotAction.UpdateRemark(newRemark.ifBlank { null }))
                    internalNavController.popBackStack()
                }
            }
        }

        composable(InternalNavRoutes.DELETE_CONFIRM) {
            HandleEditUiState(uiState) { timeSlot ->
                DeleteConfirmScreen(
                    detail = stringResource(
                        R.string.edit_timeslot_display_name,
                        timeSlot.startTime ?: stringResource(R.string.unknown),
                        timeSlot.endTime ?: stringResource(R.string.unknown)
                    ), onConfirm = {
                        viewModel.onAction(EditTimeSlotAction.Delete)
                        navController.popBackStack()
                    }, onCancel = {
                        internalNavController.popBackStack()
                    })
            }
        }
    }
}