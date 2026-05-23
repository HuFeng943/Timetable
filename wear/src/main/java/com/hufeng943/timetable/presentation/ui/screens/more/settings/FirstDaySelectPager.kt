package com.hufeng943.timetable.presentation.ui.screens.more.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.data.FirstDayOfTheWeek
import com.hufeng943.timetable.presentation.ui.common.AppConfig

@Composable
fun FirstDaySelectPager(
    config: AppConfig, onFirstDaySelect: (FirstDayOfTheWeek) -> Unit
) {
    val context = LocalContext.current

    val firstDayValues = listOf(
        FirstDayOfTheWeek.SYSTEM to stringResource(R.string.settings_first_day_system),
        FirstDayOfTheWeek.MONDAY to stringResource(R.string.settings_first_day_monday),
        FirstDayOfTheWeek.SUNDAY to stringResource(R.string.settings_first_day_sunday),
        FirstDayOfTheWeek.SATURDAY to stringResource(R.string.settings_first_day_saturday)
    )

    val currentFirstDay = config.firstDayOfTheWeekSetting

    val initialIndex = firstDayValues.indexOfFirst { it.first == currentFirstDay }.coerceAtLeast(0)

    val scrollState = rememberScalingLazyListState(
        initialCenterItemIndex = initialIndex
    )
    ScreenScaffold(
        scrollState = scrollState
    ) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.settings_first_day))
                }
            }

            items(firstDayValues.size) { index ->
                val (firstDay, label) = firstDayValues[index]
                val isSelected = (firstDay == currentFirstDay)

                RadioButton(
                    selected = isSelected, onSelect = {
                        onFirstDaySelect(firstDay)
                    }, label = {
                        Text(text = label)
                    }, icon = {
                        Icon(
                            imageVector = Icons.Rounded.DateRange, contentDescription = null
                        )
                    }, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}