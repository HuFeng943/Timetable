package com.hufeng943.timetable.presentation.ui.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.MaterialTheme
import com.hufeng943.timetable.R
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

data class TimetableUi(
    val courses: List<CourseUi> = emptyList(), // 所有的课程
    val timetableId: Long = 0,
    val semesterName: String,
    val createdAt: Instant,
    val semesterStart: LocalDate,
    val semesterEnd: LocalDate?,
    val color: Color
) {
    val displayName: String
        @Composable
        get() = semesterName.ifBlank { stringResource(R.string.default_semester_name) }

    val displayColor: Color
        @Composable
        get() = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.primaryContainer
}
