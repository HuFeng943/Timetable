package com.hufeng943.timetable.shared.data.mappers

import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.isoDayNumber

fun TimeSlot.toTimeSlotEntity(courseId: Long): TimeSlotEntity {
    // LocalTime -> Int (分钟数)
    val startMinute = this.startTime.hour * 60 + this.startTime.minute
    val endMinute = this.endTime.hour * 60 + this.endTime.minute

    // DayOfWeek -> Int (isoDayNumber)
    val dayOfWeekInt = this.dayOfWeek.isoDayNumber

    // WeekPattern -> Int (ordinal)
    val recurrenceInt = this.recurrence.ordinal

    return TimeSlotEntity(
        id = this.id,
        courseId = courseId,
        dayOfWeek = dayOfWeekInt,
        startMinute = startMinute,
        endMinute = endMinute,
        recurrence = recurrenceInt,
        remark = this.remark
    )
}

fun TimeSlotEntity.toTimeSlot(): TimeSlot {
    // Int (分钟数) -> LocalTime
    val startTime = LocalTime(hour = this.startMinute / 60, minute = this.startMinute % 60)
    val endTime = LocalTime(hour = this.endMinute / 60, minute = this.endMinute % 60)

    // Int (isoDayNumber) -> DayOfWeek
    val dayOfWeek = DayOfWeek(this.dayOfWeek)

    // Int (ordinal) -> WeekPattern
    val recurrence = WeekPattern.entries.getOrElse(this.recurrence) {
        WeekPattern.EVERY_WEEK // 默认值
    }

    return TimeSlot(
        id = this.id,
        startTime = startTime,
        endTime = endTime,
        dayOfWeek = dayOfWeek,
        recurrence = recurrence,
        remark = this.remark
    )
}
