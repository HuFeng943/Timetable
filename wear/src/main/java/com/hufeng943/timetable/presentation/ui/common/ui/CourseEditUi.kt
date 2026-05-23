package com.hufeng943.timetable.presentation.ui.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.MaterialTheme
import com.hufeng943.timetable.R

data class CourseEditUi(
    val id: Long = 0,
    val name: String = "",
    val timeSlots: List<TimeSlotUi> = emptyList(),
    val location: String? = null,
    val color: Color = Color.Unspecified,
    val teacher: String? = null
) {
    val displayName: String
        @Composable
        get() = name.ifBlank { stringResource(R.string.default_course_name) }

    val displayColor: Color
        @Composable
        get() = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.primaryContainer
}
