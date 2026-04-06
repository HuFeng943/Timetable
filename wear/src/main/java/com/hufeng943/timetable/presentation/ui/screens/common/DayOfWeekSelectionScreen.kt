package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.Text
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayOfWeekSelectionScreen(
    initialDay: DayOfWeek, onDaySelected: (DayOfWeek) -> Unit
) {
    val locale = Locale.getDefault()

    val days = remember(locale) {
        DayOfWeek.entries.map { day ->
            day to day.toJavaDayOfWeek().getDisplayName(TextStyle.FULL, locale)
        }
    }

    val initialIndex = remember(days, initialDay) {
        val dayIndex = days.indexOfFirst { it.first == initialDay }.coerceAtLeast(0)
        dayIndex + 1 // 补偿 Header 占用的位置
    }

    val scrollState = rememberScalingLazyListState(
        initialCenterItemIndex = initialIndex
    )

    ScalingLazyColumn(
        state = scrollState, modifier = Modifier.fillMaxSize()
    ) {
        item { ListHeader { Text("选择星期") } }

        items(days) { (day, labelText) ->
            RadioButton(
                selected = (day == initialDay), onSelect = { onDaySelected(day) }, label = {
                    Text(text = labelText)
                }, icon = {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}