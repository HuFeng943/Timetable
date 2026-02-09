package com.hufeng943.timetable.presentation.ui.screens.detail

import com.hufeng943.timetable.shared.ui.CourseUi

data class DetailPageData(
    val currentCourse: CourseUi,
    val listCourse: List<CourseUi>
)