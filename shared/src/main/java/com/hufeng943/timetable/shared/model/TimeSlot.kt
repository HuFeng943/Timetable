package com.hufeng943.timetable.shared.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val id: Long = 0,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val dayOfWeek: DayOfWeek? = null,
    val recurrence: WeekPattern = WeekPattern.EVERY_WEEK, // 默认每周重复
    val remark: String? = null
)