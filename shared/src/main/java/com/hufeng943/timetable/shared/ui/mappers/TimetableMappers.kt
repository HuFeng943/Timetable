package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.ui.CourseUi
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

/**
 * 返回日期在学期开始的第几周
 * @return 学期中的周次，从 1 开始计数。如果日期在学期开始之前，则返回 0
 */
fun Timetable.getWeekIndexForDate(date: LocalDate): Int {
    val offsetDays = (date.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
    val currentMonday = date.minus(offsetDays.toLong(), DateTimeUnit.DAY)

    val daysBetween = currentMonday.toEpochDays() - this.semesterStartMonday.toEpochDays()
    if (daysBetween < 0) return 0
    return (daysBetween / 7 + 1).toInt()
}

fun Timetable.toDayCoursesUi(
    targetDayOfWeek: DayOfWeek,
    weekIndex: Int
): List<CourseUi> {
    if (weekIndex == 0) return emptyList()
    return allCourses.flatMap { course -> course.toDayCoursesUi(targetDayOfWeek, weekIndex) }
}