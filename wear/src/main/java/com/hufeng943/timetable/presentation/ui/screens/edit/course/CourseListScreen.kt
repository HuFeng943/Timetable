package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hufeng943.timetable.presentation.ui.NavRoutes.editCourse
import com.hufeng943.timetable.presentation.ui.NavRoutes.listTimeSlot
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.components.HandleEditUiState
import com.hufeng943.timetable.presentation.viewmodel.edit.course.CourseListViewModel

@Composable
fun CourseListScreen(
    viewModel: CourseListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current

    HandleEditUiState(uiState) { data ->
        CourseListPager(courses = data.courses, onAddCourse = {
            navController.navigateSingle(editCourse(data.timetableId))
        }, onCourseClick = { courseId ->
            navController.navigateSingle(listTimeSlot(courseId))
        }, onCourseLongClick = { courseId ->
            navController.navigateSingle(editCourse(data.timetableId, courseId))
        })
    }
}
