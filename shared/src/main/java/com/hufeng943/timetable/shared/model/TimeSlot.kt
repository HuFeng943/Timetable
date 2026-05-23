package com.hufeng943.timetable.shared.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val id: Long = 0,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null,
    val dayOfWeek: DayOfWeek? = null,
    val recurrence: WeekPattern = WeekPattern.EVERY_WEEK, // 默认每周重复
    val remark: String? = null
)

/**
 * 按每周的第一天排序，同一天按开始时间排序。
 *
 * 排序规则：
 * 1. 先按 [firstDayOfWeek] 到其后六天的顺序排列
 * 2. 同一天内按 [startTime] 从小到大排列
 * 3. dayOfWeek 或 startTime 为 null 的排到最后
 *
 * @param firstDayOfWeek 每周的第一天（如 MONDAY、SUNDAY 等）
 */
fun List<TimeSlot>.sortedByWeekOrder(firstDayOfWeek: DayOfWeek): List<TimeSlot> {
    return this.sortedWith(
        compareBy<TimeSlot> { slot ->
            slot.dayOfWeek?.let { (it.ordinal - firstDayOfWeek.ordinal + 7) % 7 }
                ?: Int.MAX_VALUE
        }.thenBy { slot ->
            slot.startTime?.let { it.hour * 60 + it.minute }
                ?: Int.MAX_VALUE
        }
    )
}