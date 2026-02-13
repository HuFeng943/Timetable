package com.hufeng943.timetable.presentation.viewmodel.edit.timeslot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.toSafeStateFlow
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class TimeSlotListViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val cId: Long? = savedStateHandle.get<String>(NavArgs.COURSE_ID)?.toLongOrNull()
    val uiState = flow {
        val id = cId ?: throw AppError.InvalidParameter(NavArgs.COURSE_ID)
        emitAll(repository.getCourseById(id))
    }.map { course ->
        if (course == null) {
            throw AppError.CourseNotFound(cId)
        } else {
            UiState.Success(course)
        }
    }.toSafeStateFlow(viewModelScope)
}