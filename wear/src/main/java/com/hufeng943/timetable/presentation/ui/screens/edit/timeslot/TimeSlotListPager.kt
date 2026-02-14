package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
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
import com.hufeng943.timetable.shared.model.TimeSlot

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
            state = scrollState, modifier = Modifier.fillMaxSize(), contentPadding = contentPadding
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
                    TitleCard(
                        onClick = { onTimeSlotClick(timeSlot.id) },
                        title = { Text(timeSlot.startTime.toString()) },
                        subtitle = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
