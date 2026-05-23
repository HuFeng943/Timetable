package com.hufeng943.timetable.presentation.ui.common.ui.mappers

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi
import com.hufeng943.timetable.presentation.ui.common.ui.TimetableUi
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

fun Timetable.toTimetableUi(courses: List<Course>): TimetableUi = TimetableUi(
    courses = courses.map { it.toCourseUi() },
    timetableId = timetableId,
    semesterName = semesterName,
    createdAt = createdAt,
    semesterStart = semesterStart,
    semesterEnd = semesterEnd,
    color = if (this.color == -1L) Color.Unspecified else Color(this.color)
)

fun TimetableUi.toTimetable(): Timetable = Timetable(
    timetableId = timetableId,
    semesterName = semesterName,
    createdAt = createdAt,
    semesterStart = semesterStart,
    semesterEnd = semesterEnd,
    allCourses = emptyList(),
    color = if (color == Color.Unspecified) -1L else (color.toArgb().toLong() and 0xFFFFFFFFL)
)

/**
 * 返回日期在学期开始的第几周
 * @return 学期中的周次，从 1 开始计数。如果日期在学期开始之前，则返回 0
 */
fun TimetableUi.getWeekIndexForDate(date: LocalDate): Int {
    val offsetDays = (date.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
    val currentMonday = date.minus(offsetDays.toLong(), DateTimeUnit.DAY)

    val daysBetween = currentMonday.toEpochDays() - this.semesterStartMonday.toEpochDays()
    if (daysBetween < 0) return 0
    return (daysBetween / 7 + 1).toInt()
}

/**
 * 计算本学期第一天所在周的周一日期
 * 用于计算所求天数是学期的第几天
 */
val TimetableUi.semesterStartMonday: LocalDate
    get() {
        val offsetDays =
            (semesterStart.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
        return semesterStart.minus(offsetDays.toLong(), DateTimeUnit.DAY)
    }

fun TimetableUi.toDayCoursesUi(
    targetDayOfWeek: DayOfWeek,
    weekIndex: Int
): List<CourseUi> {
    if (weekIndex == 0) return emptyList()

    return courses.flatMap { course ->
        course.timeSlots.filter { slot ->
            slot.dayOfWeek == targetDayOfWeek &&
                    when (slot.recurrence) {
                        WeekPattern.EVERY_WEEK -> true
                        WeekPattern.ODD_WEEK -> weekIndex % 2 != 0
                        WeekPattern.EVEN_WEEK -> weekIndex % 2 == 0
                    }
        }.map { slot ->
            course.copy(selectedTimeSlot = slot)
        }
    }
}
