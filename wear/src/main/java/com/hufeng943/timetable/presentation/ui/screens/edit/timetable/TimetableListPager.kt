package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

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
import com.hufeng943.timetable.presentation.ui.components.edit.EditTimetableCard
import com.hufeng943.timetable.shared.model.Timetable

@Composable
fun TimetableListPager(
    timetables: List<Timetable>,
    onAddTimetable: () -> Unit,
    onTimetableClick: (Long) -> Unit,
    onTimetableLongClick: (Long) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(
            onClick = onAddTimetable
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.edit_timetable_add)
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
                    Text(stringResource(R.string.edit_timetable_title))
                }
            }
            if (timetables.isEmpty()) {
                item {
                    Text(stringResource(R.string.edit_timetable_empty))
                }
            } else {
                items(timetables, key = { it.timetableId }) { timetable ->
                    EditTimetableCard(onTimetableClick, timetable, onTimetableLongClick)
                }
            }
        }
    }
}