package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.data.TimeFormat
import com.hufeng943.timetable.presentation.ui.common.AppConfig

@Composable
fun TimeFormatSelectPager(
    config: AppConfig,
    onTimeFormatSelect: (TimeFormat) -> Unit
) {
    val timeFormats = listOf(
        TimeFormat.SYSTEM to stringResource(R.string.settings_time_format_system),
        TimeFormat.H12 to stringResource(R.string.settings_time_format_12h),
        TimeFormat.H24 to stringResource(R.string.settings_time_format_24h)
    )

    val currentFormat = config.timeFormatSetting

    val initialIndex = timeFormats.indexOfFirst { it.first == currentFormat }.coerceAtLeast(0)

    val scrollState = rememberScalingLazyListState(
        initialCenterItemIndex = initialIndex
    )

    ScalingLazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
    ) {
        item {
            ListHeader {
                Text(stringResource(R.string.settings_time_format))
            }
        }

        items(timeFormats.size) { index ->
            val (format, label) = timeFormats[index]
            val isSelected = (format == currentFormat)

            RadioButton(
                selected = isSelected,
                onSelect = {
                    onTimeFormatSelect(format)
                },
                label = {
                    Text(text = label)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
