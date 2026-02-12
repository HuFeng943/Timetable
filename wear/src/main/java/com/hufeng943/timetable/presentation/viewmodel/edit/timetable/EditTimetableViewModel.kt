package com.hufeng943.timetable.presentation.viewmodel.edit.timetable

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
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
    private val _uiState = MutableStateFlow<UiState<Timetable>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val data = tId?.let { repository.getTimetableById(it).firstOrNull() } ?: Timetable(
                    semesterName = "课表",
                    createdAt = Clock.System.now(),
                    semesterStart = Clock.System.todayIn(TimeZone.currentSystemDefault()),
                    color = 0xFFE57373
                )
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    fun onAction(action: EditTimetableAction) {
        when (action) {
            is EditTimetableAction.UpdateName -> updateSuccessState { it.copy(semesterName = action.name) }

            // 确保 semesterEnd semesterStart 别出错
            is EditTimetableAction.UpdateStartDate -> updateSuccessState { current ->
                val newEndDate = current.semesterEnd?.let { end ->
                    if (action.date > end) action.date else end
                }
                current.copy(semesterStart = action.date, semesterEnd = newEndDate)
            }

            is EditTimetableAction.UpdateEndDate -> updateSuccessState { current ->
                val newStartDate = action.date?.let { end ->
                    if (end < current.semesterStart) end else current.semesterStart
                } ?: current.semesterStart
                current.copy(semesterStart = newStartDate, semesterEnd = action.date)
            }

            is EditTimetableAction.UpdateColor -> updateSuccessState {
                it.copy(color = action.color.toArgb().toLong() and 0xFFFFFFFFL)
            }

            EditTimetableAction.Upsert -> upsertTimetable()
            EditTimetableAction.Delete -> deleteTimetable()
        }
    }

    // 用来更新 Success 状态
    private inline fun updateSuccessState(crossinline transform: (Timetable) -> Timetable) {
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(transform(current.data))
        }
    }


    private fun upsertTimetable() {
        viewModelScope.launch {
            try {
                val current = (uiState.value as? UiState.Success)?.data ?: return@launch
                repository.upsertTimetable(current)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    private fun deleteTimetable() {
        viewModelScope.launch {
            try {
                val timetableId =
                    (uiState.value as? UiState.Success)?.data?.timetableId ?: return@launch
                if (timetableId != 0L) {
                    repository.deleteTimetable(timetableId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}

