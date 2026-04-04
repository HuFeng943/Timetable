package com.hufeng943.timetable.presentation.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import com.hufeng943.timetable.data.TimeFormat

data class AppConfig(
    val languageTag: String? = null,
    val timeFormatSetting: TimeFormat = TimeFormat.SYSTEM,
    val is24HourFormat: Boolean = true
)

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }