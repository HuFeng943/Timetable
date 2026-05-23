package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import kotlinx.datetime.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimeText(
    time: LocalTime?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelSmall,
    amPmStyle: TextStyle = style.copy(fontSize = 10.sp, lineHeight = 10.sp),
    horizontalAlignment: Alignment.Horizontal = Alignment.End,
    placeholder: String? = null,
    isVertical: Boolean = true
) {
    val config = LocalAppConfig.current
    val is24Hour = config.is24HourFormat
    val locale = LocalLocale.current.platformLocale

    val (timeString, amPmString) = remember(time, is24Hour, locale) {
        if (time == null) {
            "--:--" to (if (is24Hour) null else "--")
        } else {
            val javaTime = java.time.LocalTime.of(time.hour, time.minute)
            val tStr = DateTimeFormatter.ofPattern(if (is24Hour) "HH:mm" else "hh:mm", locale)
                .format(javaTime)
            val aStr =
                if (is24Hour) null else DateTimeFormatter.ofPattern("a", locale).format(javaTime)
            tStr to aStr
        }
    }

    if (time == null && placeholder != null) {
        Text(text = placeholder, style = style, modifier = modifier)
    } else if (isVertical) {
        Column(
            modifier = modifier.width(IntrinsicSize.Min),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = timeString, style = style)
            if (amPmString != null) {
                Text(text = amPmString, style = amPmStyle)
            }
        }
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = timeString, style = style)
            if (amPmString != null) {
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = amPmString, style = amPmStyle)
            }
        }
    }
}
