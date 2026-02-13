package com.hufeng943.timetable.presentation.viewmodel.edit.course

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hufeng943.timetable.presentation.ui.NavArgs
import com.hufeng943.timetable.presentation.viewmodel.AppError
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.shared.data.repository.TimetableRepository
import com.hufeng943.timetable.shared.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCourseViewModel @Inject constructor(
    private val repository: TimetableRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val cId: Long? = savedStateHandle.get<String>(NavArgs.COURSE_ID)?.toLongOrNull()
    private val tId: Long? = savedStateHandle.get<String>(NavArgs.TABLE_ID)?.toLongOrNull()
    private val _uiState = MutableStateFlow<UiState<Course>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val data = cId?.let { repository.getCourseById(it).firstOrNull() }
                    ?: Course(name = "课程", color = 0xFFE57373)
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    fun onAction(action: EditCourseAction) {
        when (action) {
            is EditCourseAction.UpdateName -> updateSuccessState { it.copy(name = action.name) }
            is EditCourseAction.UpdateLocation -> updateSuccessState { it.copy(location = action.location) }
            is EditCourseAction.UpdateTeacher -> updateSuccessState { it.copy(teacher = action.teacher) }
            is EditCourseAction.UpdateColor -> updateSuccessState {
                it.copy(color = action.color.toArgb().toLong() and 0xFFFFFFFFL)
            }

            EditCourseAction.Upsert -> upsertCourse()
            EditCourseAction.Delete -> deleteCourse()
        }
    }

    // 用来更新 Success 状态
    private inline fun updateSuccessState(crossinline transform: (Course) -> Course) {
        val current = _uiState.value
        if (current is UiState.Success) {
            _uiState.value = UiState.Success(transform(current.data))
        }
    }

    private fun upsertCourse() {
        viewModelScope.launch {
            try {
                val current = (uiState.value as? UiState.Success)?.data ?: return@launch
                val tableId = tId ?: throw AppError.InvalidParameter(NavArgs.TABLE_ID)

                repository.upsertCourse(current, tableId)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }

    private fun deleteCourse() {
        viewModelScope.launch {
            try {
                val courseId = (uiState.value as? UiState.Success)?.data?.id ?: return@launch
                if (courseId != 0L) {
                    repository.deleteCourse(courseId)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e)
            }
        }
    }
}

