package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.shared.model.TimeSlot
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle

@Composable
fun TimeSlotListPager(
    timeSlots: List<TimeSlot>,
    onAddTimeSlot: () -> Unit,
    onTimeSlotClick: (timeSlotId: Long) -> Unit,
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(onClick = onAddTimeSlot) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "新增课时"
            )
        }
    }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null,
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text("课时列表")
                }
            }
            if (timeSlots.isEmpty()) {
                item {
                    Text("暂无课时")
                }
            } else {
                items(timeSlots, key = { it.id }) { timeSlot ->
                    TimeSlotCard(
                        timeSlot = timeSlot, onClick = { onTimeSlotClick(timeSlot.id) })
                }
            }
        }
    }
}

@Composable
fun TimeSlotCard(
    timeSlot: TimeSlot, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val config = LocalAppConfig.current
    val locale = config.locale
    // val is24Hour = config.is24Hour
    TitleCard(onClick = onClick, modifier = modifier.fillMaxWidth(), title = {
        Text("${timeSlot.startTime} - ${timeSlot.endTime}")
    }, subtitle = {
        val dayText = remember(timeSlot.dayOfWeek, locale) {
            timeSlot.dayOfWeek.toJavaDayOfWeek().getDisplayName(
                TextStyle.SHORT, locale
            )
        }
        Text("$dayText · ${timeSlot.recurrence.name}")
    }, content = {
        // 如果有备注就显示，没有就省下空间
        timeSlot.remark?.let {
            Text(
                text = it, maxLines = 1
            )
        }
    })
}
