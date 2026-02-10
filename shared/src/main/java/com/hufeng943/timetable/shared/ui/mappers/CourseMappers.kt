package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.WeekPattern
import com.hufeng943.timetable.shared.ui.CourseUi
import kotlinx.datetime.DayOfWeek

fun Course.toCourseUi(timeSlot: TimeSlot): CourseUi = CourseUi(
    id = this.id,
    name = name,
    timeSlot = timeSlot,
    color = this.color,
    location = this.location,
    teacher = this.teacher
)

fun Course.toListCoursesUi(): List<CourseUi> = this.timeSlots.map { slot ->
    this.toCourseUi(slot)
}

fun Course.toDayCoursesUi(
    targetDayOfWeek: DayOfWeek, weekIndex: Int
): List<CourseUi> = timeSlots.filter { it.dayOfWeek == targetDayOfWeek }.filter { slot ->
    val isOddWeek = weekIndex % 2 != 0 // 计算当前周是单周还是双周
    when (slot.recurrence) {
        WeekPattern.EVERY_WEEK -> true
        WeekPattern.ODD_WEEK -> isOddWeek
        WeekPattern.EVEN_WEEK -> !isOddWeek
    }
}.map { slot ->
    toCourseUi(slot)
}