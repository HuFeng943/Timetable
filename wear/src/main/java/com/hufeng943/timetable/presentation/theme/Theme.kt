package com.hufeng943.timetable.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.dynamicColorScheme

@Composable
fun TimeTableTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val dynamicColorScheme = remember(context) {
        dynamicColorScheme(context)
    }

    val fallbackColorScheme = ColorScheme()

    val colorScheme = dynamicColorScheme ?: fallbackColorScheme

    MaterialTheme(
        colorScheme = colorScheme, content = content
    )
}