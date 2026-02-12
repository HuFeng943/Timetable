package com.hufeng943.timetable.presentation.viewmodel.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.ui.screens.detail.DetailPageData
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.toSafeStateFlow
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi
import com.hufeng943.timetable.shared.ui.mappers.toListCoursesUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sId: Long = savedStateHandle.get<String>(NavArgs.TIME_SLOT_ID)?.toLongOrNull()
        ?: throw AppError.InvalidParameter(
            NavArgs.TIME_SLOT_ID
        )
    val detailState = repository.getCourseByTimeSlotId(sId).map { course ->
        if (course == null) throw AppError.CourseNotFound(sId)

        val currentSlot = course.timeSlots.find { it.id == sId }

        if (currentSlot == null) throw AppError.TimeSlotNotFound(sId)

        val currentCourseUi = course.toCourseUi(currentSlot)
        val listCourseUi = course.toListCoursesUi()

        UiState.Success(DetailPageData(currentCourseUi, listCourseUi))
    }.toSafeStateFlow(viewModelScope)
}