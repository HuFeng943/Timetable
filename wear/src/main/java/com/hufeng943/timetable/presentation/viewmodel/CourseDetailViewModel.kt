package com.hufeng943.timetable.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.screens.detail.DetailPageData
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi
import com.hufeng943.timetable.shared.ui.mappers.toListCourseUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sId: Long = savedStateHandle.get<String>("timeSlotId")?.toLongOrNull() ?: 0L
    val detailState = repository.getCourseByTimeSlotId(sId).map { course ->
        if (course == null) return@map UiState.Empty

        // 1. 直接从拿到的 course 对象里，通过 sId 找到那个具体的 timeSlot
        val currentSlot = course.timeSlots.find { it.id == sId }

        if (currentSlot == null) {
            UiState.Empty
        } else {
            // 2. 这里的 currentSlot 就是 TimeSlot 类型，不是 Flow 了！
            val currentCourseUi = course.toCourseUi(currentSlot)
            val listCourseUi = course.toListCourseUi()

            UiState.Success(DetailPageData(currentCourseUi, listCourseUi))
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(DEFAULT_FLOW_STOP_TIMEOUT),
        UiState.Loading
    )
}