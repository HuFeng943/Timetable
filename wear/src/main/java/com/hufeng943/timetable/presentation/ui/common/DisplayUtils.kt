package com.hufeng943.timetable.presentation.ui.common

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.MaterialTheme
import com.hufeng943.timetable.R
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.Timetable
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekPattern.toDisplayString() = when (this) {
    WeekPattern.EVERY_WEEK -> stringResource(R.string.every_week)
    WeekPattern.ODD_WEEK -> stringResource(R.string.odd_week)
    WeekPattern.EVEN_WEEK -> stringResource(R.string.even_week)
}

fun DayOfWeek.toDisplayString(textStyle: TextStyle): String =
    this.toJavaDayOfWeek().getDisplayName(textStyle, Locale.getDefault())

fun LocalTime.toDisplayString(is24Hour: Boolean): String {
    val pattern = DateFormat.getBestDateTimePattern(
        Locale.getDefault(), if (is24Hour) "Hm" else "hm"
    )

    return java.time.LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDate.toDisplayString(): String {
    val pattern = DateFormat.getBestDateTimePattern(
        Locale.getDefault(), "yMd"
    )

    return java.time.LocalDate.of(year, month.number, day)
        .format(DateTimeFormatter.ofPattern(pattern))
}

@Composable
fun Long.toColor(): Color = if (this == -1L) {
    MaterialTheme.colorScheme.primaryContainer
} else {
    Color(this)
}

val Timetable.displayName: String
    @Composable
    get() = semesterName.ifBlank { stringResource(R.string.default_semester_name) }

val Course.displayName: String
    @Composable
    get() = name.ifBlank { stringResource(R.string.default_course_name) }
