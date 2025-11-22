package com.hufeng943.timetable.shared.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.isoDayNumber
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dayOfWeek: DayOfWeek,
    val recurrence: WeekPattern = WeekPattern.EVERY_WEEK, // 默认每周重复
    val remark: String? = null
) {
    init {
        require(endTime > startTime) { "End time must be after start time." }
    }

    val id: Long
        get() = (dayOfWeek.isoDayNumber.toLong() shl 48) or
                (startTime.hour.toLong() shl 40) or
                (startTime.minute.toLong() shl 32) or
                (endTime.hour.toLong() shl 24) or
                (endTime.minute.toLong() shl 16) or
                (recurrence.ordinal.toLong() shl 8) or
                (remark?.hashCode()?.toLong() ?: 0L)
}