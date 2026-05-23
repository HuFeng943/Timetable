package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.presentation.ui.components.edit.EditTimeSlotCard

@Composable
fun TimeSlotListPager(
    timeSlots: List<TimeSlotUi>,
    onAddTimeSlot: () -> Unit,
    onTimeSlotClick: (timeSlotId: Long) -> Unit,
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(onClick = onAddTimeSlot) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.edit_timeslot_add)
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
                    Text(stringResource(R.string.edit_timeslot_title))
                }
            }
            if (timeSlots.isEmpty()) {
                item {
                    Text(stringResource(R.string.edit_timeslot_empty))
                }
            } else {
                items(timeSlots, key = { it.id }) { timeSlot ->
                    EditTimeSlotCard(
                        timeSlot = timeSlot, onClick = { onTimeSlotClick(timeSlot.id) })
                }
            }
        }
    }
}
