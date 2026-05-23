package com.hufeng943.timetable.presentation.ui.common.ui.mappers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek

fun Course.toCourseUi(selectedTimeSlot: TimeSlot? = null): CourseUi = CourseUi(
    id = this.id,
    name = name,
    timeSlots = this.timeSlots.map { it.toTimeSlotUi() },
    color = if (this.color == -1L) Color.Unspecified else Color(this.color),
    location = this.location,
    teacher = this.teacher,
    selectedTimeSlot = selectedTimeSlot?.toTimeSlotUi()
)

fun CourseUi.toCourse(): Course = Course(
    id = id,
    name = name,
    timeSlots = timeSlots.map { it.toTimeSlot() },
    location = location,
    color = if (color == Color.Unspecified) -1L else (color.toArgb().toLong() and 0xFFFFFFFFL),
    teacher = teacher
)

fun Course.toFlattenedCourseUiList(): List<CourseUi> = this.timeSlots.map { slot ->
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
