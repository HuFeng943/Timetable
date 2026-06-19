package com.hufeng943.timetable.presentation.viewmodel.edit.timeslot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.presentation.ui.common.ui.mappers.toTimeSlot
import com.hufeng943.timetable.presentation.ui.common.ui.mappers.toTimeSlotUi
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.TimeSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTimeSlotViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cId: Long? = savedStateHandle.get<String>(NavArgs.COURSE_ID)?.toLongOrNull()
    private val sId: Long? = savedStateHandle.get<String>(NavArgs.TIME_SLOT_ID)?.toLongOrNull()

    private val _uiState = MutableStateFlow<UiState<TimeSlotUi>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val domainData =
                    sId?.let { repository.getTimeSlotById(it).firstOrNull() } ?: TimeSlot()
                val courseUi = cId?.let { repository.getCourseById(it).firstOrNull() }
                    ?: throw AppError.CourseNotFound(cId)
                _uiState.value = UiState.Success(domainData.toTimeSlotUi(courseUi.color))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    fun onAction(action: EditTimeSlotAction) {
        when (action) {
            is EditTimeSlotAction.UpdateStartTime -> updateSuccessState { current ->
                val newStart = action.startTime
                val newEnd =
                    current.endTime?.let { if (newStart > it) newStart else current.endTime }
                current.copy(startTime = newStart, endTime = newEnd)
            }

            is EditTimeSlotAction.UpdateEndTime -> updateSuccessState { current ->
                val newEnd = action.endTime
                val newStart =
                    current.startTime?.let { if (newEnd < it) newEnd else current.startTime }
                current.copy(startTime = newStart, endTime = newEnd)
            }

            is EditTimeSlotAction.UpdateDayOfWeek -> updateSuccessState { it.copy(dayOfWeek = action.dayOfWeek) }
            is EditTimeSlotAction.UpdateRecurrence -> updateSuccessState { it.copy(recurrence = action.recurrence) }
            is EditTimeSlotAction.UpdateRemark -> updateSuccessState { it.copy(remark = action.remark) }
            EditTimeSlotAction.Upsert -> upsertTimeSlot()
            EditTimeSlotAction.Delete -> deleteTimeSlot()
        }
    }

    private inline fun updateSuccessState(crossinline transform: (TimeSlotUi) -> TimeSlotUi) {
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(transform(current.data))
        }
    }

    private fun upsertTimeSlot() {
        viewModelScope.launch {
            try {
                val currentUi =
                    (uiState.value as? UiState.Success)?.data ?: throw AppError.UnexpectedEmpty()
                val courseId = cId ?: throw AppError.InvalidParameter(NavArgs.COURSE_ID)

                repository.upsertTimeSlot(currentUi.toTimeSlot(), courseId)
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
