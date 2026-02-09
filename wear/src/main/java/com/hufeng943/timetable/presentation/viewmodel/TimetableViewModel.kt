package com.hufeng943.timetable.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.ui.mappers.getWeekIndexForDate
import com.hufeng943.timetable.shared.ui.mappers.toDayCourseUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock


@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository, private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val KEY_SELECTED_DATE = "selected_date"
    }

    // 全部课表
    val allTimetables = repository.getAllTimetables().map { list ->
        if (list.isEmpty()) UiState.Empty
        else UiState.Success(list)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT), UiState.Loading
    )

    // TODO 在 room 保存是否要显示的课表

    private val _selectedDate = savedStateHandle.getStateFlow<LocalDate>(
        KEY_SELECTED_DATE, Clock.System.todayIn(TimeZone.currentSystemDefault())
    )

    // 当前选中的课表要展示 UI 数据
    val currentTableUi = combine(allTimetables, _selectedDate) { state, selectedDate ->
        when (state) {
            is UiState.Loading -> UiState.Loading
            is UiState.Empty -> UiState.Empty
            is UiState.Success -> {
                val allTables = state.data
                val allDailyCourses = allTables.flatMap { table ->
                    val weekIndex = table.getWeekIndexForDate(selectedDate)
                    Log.d("weekIndex", weekIndex.toString())
                    Log.d("selectedDate.dayOfWeek", selectedDate.dayOfWeek.toString())
                    table.toDayCourseUi(selectedDate.dayOfWeek, weekIndex)
                }
                // 统一排序并重新编号
                val sortedCourses =
                    allDailyCourses.sortedBy { it.timeSlot.startTime }.mapIndexed { index, course ->
                        course.copy(dailyOrder = index + 1)
                    }
                UiState.Success(sortedCourses)
            }
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT), UiState.Loading
    )


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