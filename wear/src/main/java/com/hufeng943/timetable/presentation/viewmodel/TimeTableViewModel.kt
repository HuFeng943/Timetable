package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.shared.data.repository.TimeTableRepository
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TimeTableViewModel(
    repository: TimeTableRepository
) : ViewModel() {

    // Flow -> Compose state
    val timeTables: StateFlow<List<TimeTable>?> =
        repository.getAllTimeTables().stateIn(viewModelScope, SharingStarted.Lazily, null)
}
