package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.theme.LocalAppConfig
import kotlinx.datetime.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun TimeText(time: LocalTime) {
    val config = LocalAppConfig.current
    val is24Hour = config.is24Hour
    val locale = config.locale

    val localTime = remember(time) { java.time.LocalTime.of(time.hour, time.minute) }

    val timeStr = remember(localTime, is24Hour, locale) {
        val pattern = if (is24Hour) "HH:mm" else "hh:mm"
        DateTimeFormatter.ofPattern(pattern, locale).format(localTime)
    }

    if (is24Hour) {
        Text(
            text = timeStr, style = MaterialTheme.typography.labelSmall
        )
    } else {
        val amPm = remember(localTime, locale) {
            DateTimeFormatter.ofPattern("a", locale).format(localTime)
        }

        val aTextStyle = MaterialTheme.typography.labelSmall.copy(
            fontSize = 10.sp, lineHeight = 10.sp
        )

        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = timeStr, style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = amPm, style = aTextStyle
            )
        }
    }
}