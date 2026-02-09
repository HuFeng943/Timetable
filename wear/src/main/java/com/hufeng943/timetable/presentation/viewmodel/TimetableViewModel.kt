package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.ui.mappers.toCourseWithSlots
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val FLOW_STOP_TIMEOUT = 5000L
        const val KEY_SELECTED_TABLE_ID = "selected_timetable_id"
    }
    // Flow -> Compose state
    val timetables: StateFlow<List<Timetable>?> =
        repository.getAllTimetables().stateIn(viewModelScope, SharingStarted.Lazily, null)

    // 当前选中的课表 ID
    private val _selectedTableId = savedStateHandle.getStateFlow<Long?>(KEY_SELECTED_TABLE_ID, null)

    // 当前选中的课表 UI 数据
    val currentTableUi = combine(timetables, _selectedTableId) { tables, selectedId ->
        when {
            tables == null -> UiState.Loading
            tables.isEmpty() -> UiState.Empty
            else -> {
                val activeId = selectedId ?: tables.first().timetableId
                val table = tables.find { it.timetableId == activeId } ?: tables.first()
                UiState.Success(table to table.toCourseWithSlots())
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(FLOW_STOP_TIMEOUT), UiState.Loading)

    // 交互：切换课表
    fun selectTimetable(id: Long) {
        savedStateHandle[KEY_SELECTED_TABLE_ID] = id
    }

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