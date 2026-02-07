package com.hufeng943.timetable.presentation.ui

object NavRoutes {
    const val LOADING = "loading"
    const val MAIN = "main"
    const val LIST_TIMETABLE = "list_timetable"
    const val EDIT_TIMETABLE = "edit_timetable"
    const val COURSE_DETAIL = "course_detail/{courseId}/{timeSlotId}"

    const val ERROR = "error"
    fun courseDetail(courseId: Long, timeSlotId: Long) = "course_detail/$courseId/$timeSlotId"
    // TODO "course_detail/{timetableId}/{courseId}/{timeSlotId}"
}