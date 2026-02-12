package com.hufeng943.timetable.presentation.viewmodel

sealed class AppError : RuntimeException() {
    data class TimetableNotFound(val id: Long?) : AppError()
    data class CourseNotFound(val id: Long?) : AppError()
    data class TimeSlotNotFound(val id: Long?) : AppError()
    data class InvalidParameter(val navArgs: String) : AppError()
    data class Unknown(val original: Throwable) : AppError()
}