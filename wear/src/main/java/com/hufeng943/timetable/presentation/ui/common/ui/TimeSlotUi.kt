package com.hufeng943.timetable.presentation.ui.common.ui

import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

data class TimeSlotUi(
    val id: Long,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val dayOfWeek: DayOfWeek?,
    val recurrence: WeekPattern,
    val remark: String?
)
