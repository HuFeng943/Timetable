package com.hufeng943.timetable.presentation.ui.common.ui.mappers

import androidx.compose.ui.graphics.Color
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.shared.model.TimeSlot

fun TimeSlot.toTimeSlotUi(color: Long): TimeSlotUi = TimeSlotUi(
    id = id,
    startTime = startTime,
    endTime = endTime,
    dayOfWeek = dayOfWeek,
    recurrence = recurrence,
    remark = remark,
    color = if (color == -1L) Color.Unspecified else Color(color),
)

fun TimeSlotUi.toTimeSlot(): TimeSlot = TimeSlot(
    id = id,
    startTime = startTime,
    endTime = endTime,
    dayOfWeek = dayOfWeek,
    recurrence = recurrence,
    remark = remark
)
