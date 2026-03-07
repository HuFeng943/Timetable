package com.hufeng943.timetable.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.hufeng943.timetable.data.TimeFormat
import java.util.Locale

data class AppConfig(
    val is24Hour: Boolean = true,
    val locale: Locale = Locale.getDefault(),
    val timeFormatSetting: TimeFormat = TimeFormat.SYSTEM,
    val appLanguage: String = "system"
)

val LocalAppConfig = staticCompositionLocalOf { AppConfig() }