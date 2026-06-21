package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
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
import com.hufeng943.timetable.data.FirstDayOfTheWeek
import com.hufeng943.timetable.presentation.ui.common.AppConfig

@Composable
fun FirstDaySelectPager(
    config: AppConfig, onFirstDaySelect: (FirstDayOfTheWeek) -> Unit
) {
    val firstDayValues = listOf(
        FirstDayOfTheWeek.SYSTEM to stringResource(R.string.settings_first_day_system),
        FirstDayOfTheWeek.MONDAY to stringResource(R.string.settings_first_day_monday),
        FirstDayOfTheWeek.SUNDAY to stringResource(R.string.settings_first_day_sunday),
        FirstDayOfTheWeek.SATURDAY to stringResource(R.string.settings_first_day_saturday)
    )

    val currentFirstDay = config.firstDayOfTheWeekSetting

    val initialIndex = firstDayValues.indexOfFirst { it.first == currentFirstDay }.coerceAtLeast(0)

    val scrollState = rememberTransformingLazyColumnState(
        initialAnchorItemIndex = initialIndex + 1
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
                    Text(stringResource(R.string.settings_first_day))
                }
            }

            items(firstDayValues.size) { index ->
                val (firstDay, label) = firstDayValues[index]
                val isSelected = (firstDay == currentFirstDay)

                RadioButton(
                    selected = isSelected,
                    onSelect = {
                        onFirstDaySelect(firstDay)
                    },
                    label = {
                        Text(text = label)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.DateRange, contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
        }
    }
}