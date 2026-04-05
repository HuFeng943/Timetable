package com.hufeng943.timetable.presentation.viewmodel.edit.timeSlot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.WeekPattern
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class EditTimeSlotViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cId: Long? = savedStateHandle.get<String>(NavArgs.COURSE_ID)?.toLongOrNull()
    private val sId: Long? = savedStateHandle.get<String>(NavArgs.TIME_SLOT_ID)?.toLongOrNull()

    private val _uiState = MutableStateFlow<UiState<TimeSlot>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val data = sId?.let { repository.getTimeSlotById(it).firstOrNull() } ?: TimeSlot(
                    startTime = LocalTime(8, 0),
                    endTime = LocalTime(9, 30),
                    dayOfWeek = DayOfWeek.MONDAY,
                    recurrence = WeekPattern.EVERY_WEEK,
                    remark = null
                )
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    fun onAction(action: EditTimeSlotAction) {
        when (action) {
            is EditTimeSlotAction.UpdateStartTime -> updateSuccessState { it.copy(startTime = action.startTime) }
            is EditTimeSlotAction.UpdateEndTime -> updateSuccessState { it.copy(endTime = action.endTime) }
            is EditTimeSlotAction.UpdateDayOfWeek -> updateSuccessState { it.copy(dayOfWeek = action.dayOfWeek) }
            is EditTimeSlotAction.UpdateRecurrence -> updateSuccessState { it.copy(recurrence = action.recurrence) }
            is EditTimeSlotAction.UpdateRemark -> updateSuccessState { it.copy(remark = action.remark) }
            EditTimeSlotAction.Upsert -> upsertTimeSlot()
            EditTimeSlotAction.Delete -> deleteTimeSlot()
        }
    }

    // 用来更新 Success 状态
    private inline fun updateSuccessState(crossinline transform: (TimeSlot) -> TimeSlot) {
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(transform(current.data))
        }
    }

    private fun upsertTimeSlot() {
        viewModelScope.launch {
            try {
                val current =
                    (uiState.value as? UiState.Success)?.data ?: throw AppError.UnexpectedEmpty()
                val courseId = cId ?: throw AppError.InvalidParameter(NavArgs.COURSE_ID)

                repository.upsertTimeSlot(current, courseId)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    private fun deleteTimeSlot() {
        viewModelScope.launch {
            try {
                val slotId = (uiState.value as? UiState.Success)?.data?.id
                    ?: throw AppError.TimeSlotNotFound(null)
                if (slotId != 0L) {
                    repository.deleteTimeSlot(slotId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}