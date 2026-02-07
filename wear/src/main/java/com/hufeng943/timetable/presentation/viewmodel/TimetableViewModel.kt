package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.contract.TableAction
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.Timetable
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository
) : ViewModel() {

    // Flow -> Compose state
    val timetables: StateFlow<List<Timetable>?> =
        repository.getAllTimetables().stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onAction(action: TableAction) {
        when (action) {
            is TableAction.Add -> viewModelScope.launch {
                repository.insertTimetable(action.table)
            }
        }
    }
}
