package com.hufeng943.timetable.shared.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val id: Long,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dayOfWeek: DayOfWeek,
    val recurrence: WeekPattern = WeekPattern.EVERY_WEEK, // 默认每周重复
    val remark: String? = null
) {
    init {
        require(endTime > startTime) { "End time must be after start time." }
    }
}