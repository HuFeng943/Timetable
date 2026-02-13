package com.hufeng943.timetable.presentation.viewmodel.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.toSafeStateFlow
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.ui.mappers.getWeekIndexForDate
import com.hufeng943.timetable.shared.ui.mappers.toDayCoursesUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@HiltViewModel
class TimetableViewModel @Inject constructor(// TODO 待重构
    repository: TimetableRepository, private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val KEY_SELECTED_DATE = "selected_date"
    }

    // 全部课表
    val allTimetables = repository.getAllTimetables().map { list ->
        if (list.isEmpty()) UiState.Empty
        else UiState.Success(list)
    }.toSafeStateFlow(viewModelScope)

    // TODO 在 room 保存是否要显示的课表

    private val _selectedDate = savedStateHandle.getStateFlow<LocalDate>(
        KEY_SELECTED_DATE, Clock.System.todayIn(TimeZone.currentSystemDefault())
    )

    // 当前选中的课表要展示 UI 数据
    val dateCoursesUi = combine(allTimetables, _selectedDate) { state, selectedDate ->
        when (state) {
            is UiState.Loading -> UiState.Loading
            is UiState.Empty -> UiState.Empty
            is UiState.Error -> UiState.Error(state.throwable)
            is UiState.Success -> {
                val allTables = state.data
                val allDailyCourses = allTables.flatMap { table ->
                    val weekIndex = table.getWeekIndexForDate(selectedDate)
                    Log.d("weekIndex", weekIndex.toString())
                    Log.d("selectedDate.dayOfWeek", selectedDate.dayOfWeek.toString())
                    table.toDayCoursesUi(selectedDate.dayOfWeek, weekIndex)
                }
                // 统一排序并重新编号
                val sortedCoursesUi =
                    allDailyCourses.sortedBy { it.timeSlot.startTime }.mapIndexed { index, course ->
                        course.copy(dailyOrder = index + 1)
                    }
                UiState.Success(sortedCoursesUi)
            }
        }
    }.toSafeStateFlow(viewModelScope)

    fun updateSelectedDate(date: LocalDate) {
        savedStateHandle[KEY_SELECTED_DATE] = date
    }
}