package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle

@Composable
fun DayOfWeekSelectionScreen(
    initialDay: DayOfWeek?, onDaySelected: (DayOfWeek) -> Unit
) {
    val days = remember { DayOfWeek.entries }

    val initialIndex = remember(days, initialDay) {
        days.indexOf(initialDay).coerceAtLeast(0) + 1
        // 补偿 Header 占用的位置
    }

    val scrollState = rememberScalingLazyListState(
        initialCenterItemIndex = initialIndex
    )

    ScalingLazyColumn(
        state = scrollState, modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
    ) {
        item { ListHeader { Text(stringResource(R.string.selection_week)) } }

        items(days) { day ->
            RadioButton(
                selected = (day == initialDay), onSelect = { onDaySelected(day) }, label = {
                    Text(text = day.toDisplayString(TextStyle.FULL_STANDALONE))
                }, icon = {
                    Icon(Icons.Rounded.DateRange, contentDescription = null)
                }, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}