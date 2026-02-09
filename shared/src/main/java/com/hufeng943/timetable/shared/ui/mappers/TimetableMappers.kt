package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.ui.CourseUi
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
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

/**
 * 根据每个科目所有时间段将其展开多个列表并一一对应
 * 返回的列表是整个课表的
 * 仅是ID对应列表
 * @return 返回CourseWithSlotId
 */
fun Timetable.toCourseWithSlots(): List<CourseWithSlotId> =
    allCourses.flatMap { it.toCourseWithSlots() }

/**
 * 过滤出当天课程
 * 及ID对应列表
 *
 * @param targetDayOfWeek 星期几的课？
 * @param weekIndex 在第几周？（用来判断单双周）
 * @return 返回CourseWithSlotId
 */
fun Timetable.toDayCourseWithSlots(
    targetDayOfWeek: DayOfWeek,
    weekIndex: Int
): List<CourseWithSlotId> {
    if (weekIndex == 0) return emptyList()
    return allCourses.flatMap { course -> course.toDayCourseWithSlots(targetDayOfWeek, weekIndex) }
}

fun Timetable.toDayCourseUi(
    targetDayOfWeek: DayOfWeek,
    weekIndex: Int
): List<CourseUi> {
    if (weekIndex == 0) return emptyList()
    return allCourses.flatMap { course -> course.toDayCourseUi(targetDayOfWeek, weekIndex) }
}