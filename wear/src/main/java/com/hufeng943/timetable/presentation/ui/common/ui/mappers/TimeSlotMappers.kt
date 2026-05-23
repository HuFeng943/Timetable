package com.hufeng943.timetable.presentation.ui.common.ui.mappers

import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.shared.model.TimeSlot

fun TimeSlot.toTimeSlotUi(): TimeSlotUi = TimeSlotUi(
    id = id,
    startTime = startTime,
    endTime = endTime,
    dayOfWeek = dayOfWeek,
    recurrence = recurrence,
    remark = remark
)

fun TimeSlotUi.toTimeSlot(): TimeSlot = TimeSlot(
    id = id,
    startTime = startTime,
    endTime = endTime,
    dayOfWeek = dayOfWeek,
    recurrence = recurrence,
    remark = remark
)
