package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.ui.mappers.toCourseWithSlots
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository, private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val KEY_SELECTED_TABLE_ID = "selected_timetable_id"
    }

    // 全部课表
    val allTimetables = repository.getAllTimetables().map { list ->
        if (list.isEmpty()) UiState.Empty
        else UiState.Success(list)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        UiState.Loading
    )


    // 当前选中的课表 ID
    private val _selectedTableId = savedStateHandle.getStateFlow<Long?>(KEY_SELECTED_TABLE_ID, null)

    // 当前选中的课表 UI 数据
    val currentTableUi = combine(allTimetables, _selectedTableId) { state, selectedId ->
        when (state) {
            is UiState.Success -> {
                val tables = state.data
                // 找选中的 ID，找不到就默认第一个
                val activeTable = tables.find { it.timetableId == selectedId } ?: tables.first()
                UiState.Success(activeTable to activeTable.toCourseWithSlots())
            }

            is UiState.Loading -> UiState.Loading
            is UiState.Empty -> UiState.Empty
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        UiState.Loading
    )

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