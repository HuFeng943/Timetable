package com.hufeng943.timetable.presentation.ui

object NavArgs {
    const val TIME_SLOT_ID = "timeSlotId"
    const val TABLE_ID = "tableId"
    const val COURSE_ID = "courseId"
}

object NavRoutes {
    const val MAIN = "main"

    //----------------------------------
    const val COURSE_DETAIL = "course_detail/{${NavArgs.TIME_SLOT_ID}}"
    fun courseDetail(timeSlotId: Long) = "course_detail/$timeSlotId"

    //----------------------------------
    const val LIST_TIMETABLE = "list_timetable"
    const val EDIT_TIMETABLE = "edit_timetable/{${NavArgs.TABLE_ID}}"
    fun editTimetable(timetableId: Long? = null) = "edit_timetable/$timetableId"

    //----------------------------------
    const val LIST_COURSE = "list_course/{${NavArgs.TABLE_ID}}"
    fun listCourse(timetableId: Long) = "list_course/$timetableId"
    const val EDIT_COURSE = "edit_course/{${NavArgs.TABLE_ID}}/{${NavArgs.COURSE_ID}}"
    fun editCourse(timetableId: Long, courseId: Long? = null) = "edit_course/$timetableId/$courseId"

    //----------------------------------
    const val LIST_TIMESLOT = "list_timeslot/{${NavArgs.COURSE_ID}}"
    fun listTimeSlot(courseId: Long) = "list_timeslot/$courseId"
}