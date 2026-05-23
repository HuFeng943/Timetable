package com.hufeng943.timetable.shared.model

import com.hufeng943.timetable.shared.serializer.InstantAsLongSerializer
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * 一个课程表，课程表里会有多个科目
 * timetableId应该确保唯一性
 */
@Serializable
data class Timetable(
    val timetableId: Long = 0,
    val semesterName: String = "", // 课程表的名称
    @Serializable(with = InstantAsLongSerializer::class)// 这是kotlin.time.Instant
    val createdAt: Instant,
    val semesterStart: LocalDate, // 课表开始日期
    val semesterEnd: LocalDate? = null, // 课表结束日期,有可能永不结束
    val allCourses: List<Course> = emptyList(),
    val color: Long = -1L
) {
    init {
        require(
            semesterEnd == null || semesterStart <= semesterEnd
        ) { "Invalid semester dates." }
    }

    /**
     * 计算本学期第一天所在周的周一日期
     * 用于计算所求天数是学期的第几天
     */
    val semesterStartMonday: LocalDate by lazy {
        val offsetDays =
            (semesterStart.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
        semesterStart.minus(offsetDays.toLong(), DateTimeUnit.DAY)
    }
}
