// 别问，问就是Android Studio 让我加的
@file:OptIn(kotlin.time.ExperimentalTime::class, kotlinx.serialization.ExperimentalSerializationApi::class)
@file:UseContextualSerialization(kotlinx.datetime.LocalDate::class, kotlin.time.Instant::class)

package com.hufeng943.timetable.shared

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import kotlinx.datetime.LocalTime
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlin.time.Duration
import kotlin.time.Instant

@Serializable
enum class RecurrenceType {//单双周课程
    WEEKLY,
    FIRST_WEEK,
    SECOND_WEEK
}

@Serializable
data class TimeSlot(
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dayOfWeek: DayOfWeek
) {
    init {
        require(endTime > startTime) { "End time must be after start time." }
    }
}

@Serializable
data class Course(
    val id: Long = 0,
    val name: String,
    val timeSlots: List<TimeSlot>,
    val recurrence: RecurrenceType = RecurrenceType.WEEKLY, // 默认每周重复
    val location: String?,
    val color: Int = 0xFF2196F3.toInt(), // 默认蓝色
    val teacher: String?
) {
    init {
        require(name.isNotBlank()) { "Course name cannot be blank." }
        require(timeSlots.isNotEmpty()) { "Course must have at least one time slot." }
    }
}

@Serializable
data class UserTimeTable(
    val allCourses: List<Course>,
    val timeTableId: Long = 0,
    val semesterName: String, // 课程表的名称
    val createdAt: Instant,
    val semesterStart: LocalDate // 课表开始日期
) {
    init {
        require(semesterName.isNotBlank()) { "Course name cannot be blank." }
    }
    // TODO - 应该要优化一下这里的
    fun getCoursesForDate(date: LocalDate): List<Pair<TimeSlot, Course>> {
        // 学期开始日当天距离周一的距离
        val daysSinceMonday = semesterStart.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
        val logicalSemesterStartMondayEpoch = semesterStart.toEpochDays() - daysSinceMonday
        // 同理，弄个目标日期距离周一的距离
        val targetDaysSinceMonday = date.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber
        val logicalCurrentMondayEpoch = date.toEpochDays() - targetDaysSinceMonday
        // 算出自学期开始周的周一（起始周一）到当前日期所在周的周一（当前周一）之间间隔的总天数
        val daysBetweenLogicalMondays = logicalCurrentMondayEpoch - logicalSemesterStartMondayEpoch
        // 学期过了多少周
        val currentWeekIndex = (daysBetweenLogicalMondays / 7 + 1).toInt()
        // 上面一大坨其实就是为了判断单双周
        val targetRecurrence = if (currentWeekIndex % 2 != 0) RecurrenceType.FIRST_WEEK else RecurrenceType.SECOND_WEEK

        val slotsForToday = mutableListOf<Pair<TimeSlot, Course>>() //返回用

        allCourses.forEach { course ->
            if (course.recurrence == RecurrenceType.WEEKLY || course.recurrence == targetRecurrence) {
                course.timeSlots.filter { it.dayOfWeek == date.dayOfWeek}.forEach {
                    slot -> slotsForToday.add(slot to course)
                }
            }
        }

        // 排序 课程开始时间
        return slotsForToday.sortedBy { it.first.startTime }
    }
}

// 感觉这个类型做的好烂，，， 就这样吧