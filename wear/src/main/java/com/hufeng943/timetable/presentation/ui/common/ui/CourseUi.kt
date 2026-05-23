package com.hufeng943.timetable.presentation.ui.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.MaterialTheme
import com.hufeng943.timetable.R

/**
 * 用于在UI层面显示课程
 * 可以表示课程的完整定义（带多个 timeSlots）
 * 也可以表示课程的一个具体节次（通过 selectedTimeSlot 标识）
 */
data class CourseUi(
    val id: Long,
    val name: String,
    val timeSlots: List<TimeSlotUi> = emptyList(),
    val color: Color,
    val location: String?,
    val teacher: String?,
    val dailyOrder: Int? = null,
    private val selectedTimeSlot: TimeSlotUi? = null
) {
    /**
     * 为保持兼容性提供的便捷属性。
     * 如果是具体节次展示，返回 selectedTimeSlot；
     * 否则返回第一个 timeSlot。
     */
    val timeSlot: TimeSlotUi
        get() = selectedTimeSlot ?: timeSlots.firstOrNull()
        ?: throw IllegalStateException("Course has no time slots")

    val displayName: String
        @Composable
        get() = name.ifBlank { stringResource(R.string.default_course_name) }

    val displayColor: Color
        @Composable
        get() = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.primaryContainer

    val displayLocation: String
        @Composable
        get() = location ?: stringResource(R.string.not_set)

    val displayTeacher: String
        @Composable
        get() = teacher ?: stringResource(R.string.not_set)
}
