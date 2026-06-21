package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.hufeng943.timetable.R
import com.hufeng943.timetable.data.TimeFormat
import com.hufeng943.timetable.presentation.ui.common.AppConfig

@Composable
fun TimeFormatSelectPager(
    config: AppConfig, onTimeFormatSelect: (TimeFormat) -> Unit
) {
    val timeFormats = listOf(
        TimeFormat.SYSTEM to stringResource(R.string.settings_time_format_system),
        TimeFormat.H12 to stringResource(R.string.settings_time_format_12h),
        TimeFormat.H24 to stringResource(R.string.settings_time_format_24h)
    )

    val currentFormat = config.timeFormatSetting

    val initialIndex = timeFormats.indexOfFirst { it.first == currentFormat }.coerceAtLeast(0)

    val scrollState = rememberTransformingLazyColumnState(
        initialAnchorItemIndex = initialIndex
    )
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(
        scrollState = scrollState
    ) { contentPadding ->
        TransformingLazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ListHeaderDefaults.minimumTopListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text(stringResource(R.string.settings_time_format))
                }
            }

            items(timeFormats) { (format, label) ->
                val isSelected = (format == currentFormat)

                RadioButton(
                    selected = isSelected, onSelect = {
                        onTimeFormatSelect(format)
                    }, label = {
                        Text(text = label)
                    }, icon = {
                        Icon(
                            imageVector = Icons.Rounded.AccessTime, contentDescription = null
                        )
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
        }
    }
}