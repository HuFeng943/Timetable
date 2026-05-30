package com.hufeng943.timetable.presentation.viewmodel.detail

import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi

data class DetailPageData(
    val timetableId: Long,
    val currentCourseUi: CourseUi,
    val listCourseUi: List<CourseUi>
)