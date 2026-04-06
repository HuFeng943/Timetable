package com.hufeng943.timetable.presentation.ui.common

import com.hufeng943.timetable.shared.model.WeekPattern

fun WeekPattern.toDisplayString() = when (this) {
    WeekPattern.EVERY_WEEK -> "每周"
    WeekPattern.ODD_WEEK -> "单周"
    WeekPattern.EVEN_WEEK -> "双周"
}
