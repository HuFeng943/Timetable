package com.hufeng943.timetable.presentation.ui.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.MaterialTheme
import com.hufeng943.timetable.R

/**
 * 用于在UI层面显示课程
 * 因为只有一个timeSlot所以对应的是每节课
 * 可以由Course转换而来
 */
data class CourseUi(
    val id: Long,
    val name: String,
    val timeSlot: TimeSlotUi,
    val color: Color,
    val location: String?,
    val teacher: String?,
    val dailyOrder: Int? = null
) {
    val displayName: String
        @Composable
        get() = name.ifBlank { stringResource(R.string.default_course_name) }

    val displayColor: Color
        @Composable
        get() = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.primaryContainer
}
