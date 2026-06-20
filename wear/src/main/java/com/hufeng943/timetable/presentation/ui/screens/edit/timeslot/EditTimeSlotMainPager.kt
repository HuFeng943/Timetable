package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.hufeng943.timetable.presentation.ui.common.ui.TimeSlotUi
import com.hufeng943.timetable.presentation.ui.components.DeleteButton
import com.hufeng943.timetable.presentation.ui.components.TimeText
import com.hufeng943.timetable.presentation.ui.components.toDisplayString
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

    val canSave =
        timeSlot.startTime != null && timeSlot.endTime != null && timeSlot.dayOfWeek != null
    ScreenScaffold(
        scrollState = scrollState, edgeButton = {
            EdgeButton(onClick = onSave, enabled = canSave) {
                Icon(Icons.Rounded.Check, contentDescription = stringResource(R.string.check))
            }
        }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    AnimatedContent(
                        targetState = timeSlot.id == 0L,
                        label = "header_text"
                    ) { isAdd ->
                        Text(
                            if (isAdd) stringResource(R.string.edit_timeslot_add) else stringResource(
                                R.string.edit_timeslot_edit
                            )
                        )
                    }
                }
            }

            // 开始时间
            item {
                TitleCard(
                    onClick = onStartTimeClick, title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.AccessTime, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timeslot_start),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }) {
                    AnimatedContent(
                        targetState = timeSlot.startTime,
                        label = "start_time"
                    ) { startTime ->
                        TimeText(
                            time = startTime,
                            style = MaterialTheme.typography.labelLarge,
                            placeholder = stringResource(R.string.not_set),
                            isVertical = false
                        )
                    }
                }
            }

            // 结束时间
            item {
                TitleCard(
                    onClick = onEndTimeClick, title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.AccessTime, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timeslot_end),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }) {
                    AnimatedContent(targetState = timeSlot.endTime, label = "end_time") { endTime ->
                        TimeText(
                            time = endTime,
                            style = MaterialTheme.typography.labelLarge,
                            placeholder = stringResource(R.string.not_set),
                            isVertical = false
                        )
                    }
                }
            }

            // 星期
            item {
                TitleCard(
                    onClick = onDayOfWeekClick, title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.DateRange, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timeslot_week),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }) {
                    AnimatedContent(
                        targetState = timeSlot.dayOfWeek?.toDisplayString(TextStyle.FULL_STANDALONE)
                            ?: stringResource(R.string.not_set),
                        label = "day_of_week"
                    ) { dayOfWeekStr ->
                        Text(
                            text = dayOfWeekStr, style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            // 重复规则
            item {
                TitleCard(
                    onClick = onRecurrenceClick, title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timeslot_repeat),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }) {
                    AnimatedContent(
                        targetState = timeSlot.recurrence.toDisplayString(),
                        label = "recurrence"
                    ) { recurrenceStr ->
                        Text(
                            text = recurrenceStr,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            // 备注
            item {
                TitleCard(
                    onClick = onRemarkClick,
                    onLongClick = onRemarkLongClick,
                    subtitle = {
                        AnimatedVisibility(visible = timeSlot.remark != null) {
                            Text(stringResource(R.string.clear_long_press))
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Rounded.Notes, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timeslot_remark),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }) {
                    AnimatedContent(
                        targetState = timeSlot.displayRemark,
                        label = "remark"
                    ) { remark ->
                        Text(
                            text = remark, style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            if (timeSlot.id != 0L) {
                item {
                    DeleteButton(
                        label = stringResource(R.string.edit_timeslot_delete), onClick = onDelete
                    )
                }
            }
        }
    }
}
