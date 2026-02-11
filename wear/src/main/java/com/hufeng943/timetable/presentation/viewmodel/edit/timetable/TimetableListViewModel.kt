package com.hufeng943.timetable.presentation.viewmodel.edit.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.viewmodel.DEFAULT_FLOW_STOP_TIMEOUT
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimetableListViewModel @Inject constructor(
    repository: TimetableRepository
) : ViewModel() {
    val timetables = repository.getAllTimetables().map { list ->
        if (list.isEmpty()) UiState.Empty
        else UiState.Success(list)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT), UiState.Loading
    )
}