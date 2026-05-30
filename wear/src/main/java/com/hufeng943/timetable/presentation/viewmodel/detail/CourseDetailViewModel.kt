package com.hufeng943.timetable.presentation.viewmodel.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.ui.common.ui.mappers.toCourseUi
import com.hufeng943.timetable.presentation.ui.common.ui.mappers.toFlattenedCourseUiList
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.toSafeStateFlow
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val sId: Long = savedStateHandle.get<String>(NavArgs.TIME_SLOT_ID)?.toLongOrNull()
        ?: throw AppError.InvalidParameter(
            NavArgs.TIME_SLOT_ID
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val detailState = repository.getCourseByTimeSlotId(sId).flatMapLatest { course ->
        if (course == null) {
            throw AppError.TimeSlotNotFound(sId)
        } else {
            repository.getTimetableByCourseId(course.id).map { timetable ->
                val currentSlot =
                    course.timeSlots.find { it.id == sId } ?: throw AppError.TimeSlotNotFound(sId)

                val currentCourseUi = course.toCourseUi(currentSlot)
                val flattenedCourseUiList = course.toFlattenedCourseUiList()

                UiState.Success(
                    DetailPageData(
                        timetableId = timetable?.timetableId
                            ?: throw AppError.CourseNotFound(course.id),
                        currentCourseUi = currentCourseUi,
                        listCourseUi = flattenedCourseUiList
                    )
                )
            }
        }
    }.toSafeStateFlow(viewModelScope)
}
