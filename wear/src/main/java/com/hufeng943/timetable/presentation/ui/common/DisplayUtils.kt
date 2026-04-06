package com.hufeng943.timetable.presentation.ui.common

import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle
import java.util.Locale

fun WeekPattern.toDisplayString() = when (this) {
    WeekPattern.EVERY_WEEK -> "每周"
    WeekPattern.ODD_WEEK -> "单周"
    WeekPattern.EVEN_WEEK -> "双周"
}

fun DayOfWeek.toDisplayString(textStyle: TextStyle) =
    this.toJavaDayOfWeek().getDisplayName(textStyle, Locale.getDefault())