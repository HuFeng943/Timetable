package com.hufeng943.timetable.presentation.ui.screens.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hufeng943.timetable.presentation.ui.NavRoutes.editCourse
import com.hufeng943.timetable.presentation.ui.NavRoutes.listTimeSlot
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.components.HandleEditUiState
import com.hufeng943.timetable.presentation.viewmodel.detail.CourseDetailViewModel

@Composable
fun CourseDetailScreen(
    viewModel: CourseDetailViewModel = hiltViewModel()
) {
    val uiDetailState by viewModel.detailState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    HandleEditUiState(uiDetailState) { data ->
        DetailsPager(courseUi = data.currentCourseUi, onCourseClick = {
            navController.navigateSingle(
                editCourse(
                    data.timetableId, data.currentCourseUi.id
                )
            )
        }, onCourseLongClick = {
            navController.navigateSingle(
                listTimeSlot(
                    data.currentCourseUi.id
                )
            )
        })
    }
}