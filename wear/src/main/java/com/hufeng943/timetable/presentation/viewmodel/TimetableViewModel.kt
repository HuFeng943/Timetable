package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.Timetable
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class TableAction {
    data class Upsert(val table: Timetable) : TableAction()
    data class Delete(val timetableId: Long) : TableAction()
    // TODO ......
}

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository
) : ViewModel() {

    // Flow -> Compose state
    val timetables: StateFlow<List<Timetable>?> =
        repository.getAllTimetables().stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun onAction(action: TableAction) {
        when (action) {
            is TableAction.Upsert -> viewModelScope.launch {
                repository.upsertTimetable(action.table)
            }
            is TableAction.Delete -> viewModelScope.launch {
                repository.deleteTimetable(action.timetableId)
            }
        }
    }
}
