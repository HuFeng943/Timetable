package com.hufeng943.timetable.presentation.viewmodel.edit.timetable

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.ui.common.ui.TimetableUi
import com.hufeng943.timetable.presentation.ui.common.ui.mappers.toTimetable
import com.hufeng943.timetable.presentation.ui.common.ui.mappers.toTimetableUi
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.Timetable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class EditTimetableViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tId: Long? = savedStateHandle.get<String>(NavArgs.TABLE_ID)?.toLongOrNull()
    val toDay = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val _uiState = MutableStateFlow<UiState<TimetableUi>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val domainData = tId?.let { repository.getTimetableById(it).firstOrNull() }
                    ?: Timetable(
                        createdAt = Clock.System.now(),
                        semesterStart = Clock.System.todayIn(TimeZone.currentSystemDefault())
                    )
                _uiState.value = UiState.Success(domainData.toTimetableUi(domainData.allCourses))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    fun onAction(action: EditTimetableAction) {
        when (action) {
            is EditTimetableAction.UpdateName -> updateSuccessState { it.copy(semesterName = action.name) }

            is EditTimetableAction.UpdateStartDate -> updateSuccessState { current ->
                val newEndDate = current.semesterEnd?.let { end ->
                    if ((action.date ?: toDay) > end) action.date else end
                }
                current.copy(semesterStart = action.date ?: toDay, semesterEnd = newEndDate)
            }

            is EditTimetableAction.UpdateEndDate -> updateSuccessState { current ->
                val newStartDate = action.date?.let { end ->
                    if (end < current.semesterStart) end else current.semesterStart
                } ?: current.semesterStart
                current.copy(semesterStart = newStartDate, semesterEnd = action.date)
            }

            is EditTimetableAction.UpdateColor -> updateSuccessState {
                it.copy(color = action.color ?: Color.Unspecified)
            }

            EditTimetableAction.Upsert -> upsertTimetable()
            EditTimetableAction.Delete -> deleteTimetable()
        }
    }

    private inline fun updateSuccessState(crossinline transform: (TimetableUi) -> TimetableUi) {
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(transform(current.data))
        }
    }

    private fun upsertTimetable() {
        viewModelScope.launch {
            try {
                val currentUi =
                    (uiState.value as? UiState.Success)?.data ?: throw AppError.UnexpectedEmpty()
                repository.upsertTimetable(currentUi.toTimetable())
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    private fun deleteTimetable() {
        viewModelScope.launch {
            try {
                val timetableId = (uiState.value as? UiState.Success)?.data?.timetableId
                    ?: throw AppError.TimetableNotFound(null)
                if (timetableId != 0L) {
                    repository.deleteTimetable(timetableId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}
