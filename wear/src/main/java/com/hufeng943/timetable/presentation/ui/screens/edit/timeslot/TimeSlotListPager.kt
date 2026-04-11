package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.components.TimeText
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle
import java.util.Locale

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
    val locale = Locale.getDefault()

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 区域 1 时间
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(top = 3.dp),
                horizontalAlignment = Alignment.End, // 右对齐
            ) {
                TimeText(time = timeSlot.startTime)
                Spacer(modifier = Modifier.height(2.dp))
                TimeText(time = timeSlot.endTime)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Top) {
                val dayText = remember(timeSlot.dayOfWeek, locale) {
                    timeSlot.dayOfWeek?.toJavaDayOfWeek()?.getDisplayName(
                        TextStyle.SHORT, locale
                    ) ?: "未知"
                }
                val recurrenceText = remember(timeSlot.recurrence) {
                    when (timeSlot.recurrence) {
                        WeekPattern.EVERY_WEEK -> "每周"
                        WeekPattern.ODD_WEEK -> "单周"
                        WeekPattern.EVEN_WEEK -> "双周"
                    }
                }
                Text(
                    text = "$recurrenceText · $dayText",
                    style = MaterialTheme.typography.titleMedium
                )

                timeSlot.remark?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}