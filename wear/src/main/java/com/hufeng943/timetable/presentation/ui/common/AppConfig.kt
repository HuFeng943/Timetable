package com.hufeng943.timetable.presentation.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import com.hufeng943.timetable.data.TimeFormat
import java.util.Locale

data class AppConfig(
    val useSystemLanguage: Boolean = true,
    val locale: Locale = Locale.getDefault(),
    val timeFormatSetting: TimeFormat = TimeFormat.SYSTEM,
    val is24HourFormat: Boolean = true
)

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }