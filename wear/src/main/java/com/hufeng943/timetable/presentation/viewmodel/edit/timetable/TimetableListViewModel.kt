package com.hufeng943.timetable.presentation.viewmodel.edit.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.toSafeStateFlow
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TimetableListViewModel @Inject constructor(
    repository: TimetableRepository
) : ViewModel() {
    val uiState = repository.getAllTimetables().map { list ->
        if (list.isEmpty()) UiState.Empty
        else UiState.Success(list)
    }.toSafeStateFlow(viewModelScope)
}