package com.hufeng943.timetable.presentation.viewmodel.edit.course

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.viewmodel.DEFAULT_FLOW_STOP_TIMEOUT
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.tableId
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tId: Long? = savedStateHandle.get<String>(tableId)?.toLongOrNull()
    val uiState = (tId?.let { repository.getTimetableById(it) } ?: emptyFlow()).map { timetable ->
        if (timetable == null) {
            UiState.Empty
        } else {
            UiState.Success(timetable)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        initialValue = UiState.Loading
    )
}