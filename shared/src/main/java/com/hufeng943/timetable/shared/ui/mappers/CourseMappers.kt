package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.WeekPattern
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import kotlinx.datetime.DayOfWeek

/**
 * 根据科目所有时间段将其展开多个列表并一一对应
 * 仅是ID对应列表
 * 返回的列表是整个科目的
 * @return 返回CourseWithSlotId
 */
fun Course.toCourseWithSlots(): List<CourseWithSlotId> = timeSlots.map { slot ->
    CourseWithSlotId(
        courseId = this.id, timeSlotId = slot.id
    )
}

/**
 * 过滤出当天课程
 * 及ID对应列表
 *
 * @param targetDayOfWeek 星期几的课？
 * @param weekIndex 在第几周？（用来判断单双周）
 * @return 返回CourseWithSlotId
 */
fun Course.toDayCourseWithSlots(targetDayOfWeek: DayOfWeek, weekIndex: Int): List<CourseWithSlotId> =
    timeSlots.filter { it.dayOfWeek == targetDayOfWeek }
        .filter { slot ->
            val isOddWeek = weekIndex % 2 != 0 // 计算当前周是单周还是双周
            when (slot.recurrence) {
                WeekPattern.EVERY_WEEK -> true
                WeekPattern.ODD_WEEK -> isOddWeek
                WeekPattern.EVEN_WEEK -> !isOddWeek
            }
        }.map { slot ->
            CourseWithSlotId(courseId = this.id, timeSlotId = slot.id)
        }