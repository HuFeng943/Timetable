package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.presentation.ui.components.DeleteButton
import com.hufeng943.timetable.presentation.ui.components.TimeText
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle

@Composable
fun EditTimeSlotMainPager(
    timeSlot: TimeSlotUi,
    onSave: () -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
    onDayOfWeekClick: () -> Unit,
    onRecurrenceClick: () -> Unit,
    onRemarkClick: () -> Unit,
    onRemarkLongClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val scrollState = rememberScalingLazyListState()

    val canSave = timeSlot.startTime != null
            && timeSlot.endTime != null
            && timeSlot.dayOfWeek != null
    ScreenScaffold(
        scrollState = scrollState,
        edgeButton = {
            EdgeButton(onClick = onSave, enabled = canSave) {
                Icon(Icons.Default.Check, contentDescription = stringResource(R.string.check))
            }
        }
    ) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null,
            state = scrollState,
            contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(
                        if (timeSlot.id == 0L) stringResource(R.string.edit_timeslot_add) else stringResource(
                            R.string.edit_timeslot_edit
                        )
                    )
                }
            }

            // 开始时间
            item {
                TitleCard(
                    onClick = onStartTimeClick,
                    title = {
                        Text(
                            stringResource(R.string.edit_timeslot_start),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                ) {
                    TimeText(
                        time = timeSlot.startTime,
                        style = MaterialTheme.typography.labelLarge,
                        placeholder = stringResource(R.string.not_set),
                        isVertical = false
                    )
                }
            }

            // 结束时间
            item {
                TitleCard(
                    onClick = onEndTimeClick,
                    title = {
                        Text(
                            stringResource(R.string.edit_timeslot_end),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                ) {
                    TimeText(
                        time = timeSlot.endTime,
                        style = MaterialTheme.typography.labelLarge,
                        placeholder = stringResource(R.string.not_set),
                        isVertical = false
                    )
                }
            }

            // 星期
            item {
                TitleCard(
                    onClick = onDayOfWeekClick,
                    title = {
                        Text(
                            stringResource(R.string.edit_timeslot_week),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                ) {
                    Text(
                        text = timeSlot.dayOfWeek?.toJavaDayOfWeek()
                            ?.getDisplayName(TextStyle.FULL, LocalLocale.current.platformLocale)
                            ?: stringResource(
                            R.string.not_set
                        ),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // 重复规则
            item {
                TitleCard(
                    onClick = onRecurrenceClick,
                    title = {
                        Text(
                            stringResource(R.string.edit_timeslot_repeat),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                ) {
                    Text(
                        text = timeSlot.recurrence.toDisplayString(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // 备注
            item {
                TitleCard(
                    onClick = onRemarkClick,
                    onLongClick = onRemarkLongClick,
                    subtitle = { if (timeSlot.remark != null) Text(stringResource(R.string.clear_long_press)) },
                    title = {
                        Text(
                            stringResource(R.string.edit_timeslot_remark),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                ) {
                    Text(
                        text = timeSlot.displayRemark,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            if (timeSlot.id != 0L) {
                item {
                    DeleteButton(
                        label = stringResource(R.string.edit_timeslot_delete),
                        onClick = onDelete
                    )
                }
            }
        }
    }
}
