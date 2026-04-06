package com.hufeng943.timetable.presentation.ui.common

import android.text.format.DateFormat
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun WeekPattern.toDisplayString() = when (this) {
    WeekPattern.EVERY_WEEK -> "每周"
    WeekPattern.ODD_WEEK -> "单周"
    WeekPattern.EVEN_WEEK -> "双周"
}

fun DayOfWeek.toDisplayString(textStyle: TextStyle): String =
    this.toJavaDayOfWeek().getDisplayName(textStyle, Locale.getDefault())

fun LocalTime.toDisplayString(is24Hour: Boolean): String {
    val pattern = DateFormat.getBestDateTimePattern(
        Locale.getDefault(),
        if (is24Hour) "Hm" else "hm"
    )

    return java.time.LocalTime.of(hour, minute)
        .format(DateTimeFormatter.ofPattern(pattern))
}