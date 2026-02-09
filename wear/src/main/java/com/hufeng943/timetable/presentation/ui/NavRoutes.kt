package com.hufeng943.timetable.presentation.ui

object NavRoutes {
    const val MAIN = "main"
    const val COURSE_DETAIL = "course_detail/{courseId}/{timeSlotId}"
    fun courseDetail(courseId: Long, timeSlotId: Long) = "course_detail/$courseId/$timeSlotId"

    // TODO "course_detail/{timetableId}/{courseId}/{timeSlotId}"
    const val LIST_TIMETABLE = "list_timetable"
    const val EDIT_TIMETABLE = "edit_timetable/{timetableId}"
    fun editTimetable(timetableId: Long? = null) = "edit_timetable/$timetableId"

}