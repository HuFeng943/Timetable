package com.hufeng943.timetable.shared.ui

import com.hufeng943.timetable.shared.model.TimeSlot

/**
 * 用于在UI层面显示课程
 * 因为只有一个timeSlot所以对应的是每节课
 * 可以由Course转换而来
 */
data class CourseUi(
    val name: String,
    val timeSlot: TimeSlot,
    val color: Long,
    val location: String?,
    val teacher: String?,
    val dailyOrder: Int? = null
) {
    init {
        require(name.isNotBlank()) { "CourseUI name cannot be blank." }
        require(timeSlot.endTime > timeSlot.startTime) { "End time must be after start time." }
    }
}