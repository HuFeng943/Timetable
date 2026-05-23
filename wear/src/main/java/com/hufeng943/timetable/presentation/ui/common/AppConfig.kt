package com.hufeng943.timetable.presentation.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import com.hufeng943.timetable.data.FirstDayOfTheWeek
import com.hufeng943.timetable.data.TimeFormat
import kotlinx.datetime.DayOfWeek

data class AppConfig(
    val languageTag: String? = null,
    val timeFormatSetting: TimeFormat = TimeFormat.SYSTEM,
    val is24HourFormat: Boolean = true,
    val firstDayOfTheWeekSetting: FirstDayOfTheWeek = FirstDayOfTheWeek.SYSTEM,
    val effectiveFirstDayOfTheWeek: DayOfWeek = DayOfWeek.MONDAY
)

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }