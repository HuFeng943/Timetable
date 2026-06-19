package com.hufeng943.timetable.presentation.ui.common.ui.mappers

import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.shared.model.TimeSlot

fun TimeSlot.toTimeSlotUi(color: Long): TimeSlotUi = TimeSlotUi(
    id = id,
    startTime = startTime,
    endTime = endTime,
    dayOfWeek = dayOfWeek,
    recurrence = recurrence,
    remark = remark,
    color = color
)

fun TimeSlotUi.toTimeSlot(): TimeSlot = TimeSlot(
    id = id,
    startTime = startTime,
    endTime = endTime,
    dayOfWeek = dayOfWeek,
    recurrence = recurrence,
    remark = remark
)
