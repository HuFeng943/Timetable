package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CollectionsBookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import com.hufeng943.timetable.presentation.ui.common.ui.TimetableUi
import com.hufeng943.timetable.presentation.ui.components.ColorPickerCard
import com.hufeng943.timetable.presentation.ui.components.DeleteButton

@Composable
fun EditTimetableMainPager(
    timetable: TimetableUi,
    onSave: () -> Unit,
    onNameClick: () -> Unit,
    onStartDateClick: () -> Unit,
    onStartDateLongClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onEndDateLongClick: () -> Unit,
    onColorClick: () -> Unit,
    onColorLongClick: () -> Unit,
    onDelete: () -> Unit,
    startDateIsToday: Boolean
) {
    val scrollState = rememberScalingLazyListState()

    ScreenScaffold(
        scrollState = scrollState, edgeButton = {
            EdgeButton(onClick = onSave) {
                Icon(Icons.Rounded.Check, contentDescription = stringResource(R.string.check))
            }
        }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    AnimatedContent(
                        targetState = timetable.timetableId == 0L,
                        label = "header_text"
                    ) { isAdd ->
                        Text(
                            if (isAdd) stringResource(R.string.edit_timetable_add) else stringResource(
                                R.string.edit_timetable_edit
                            )
                        )
                    }
                }
            }

            item { // 修改名称
                TitleCard(
                    onClick = onNameClick,
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.CollectionsBookmark, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timetable_name),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                ) {
                    AnimatedContent(
                        targetState = timetable.displayName,
                        label = "timetable_name"
                    ) { name ->
                        Text(
                            name, style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item { // 开始日期
                TitleCard(
                    onClick = onStartDateClick,
                    onLongClick = onStartDateLongClick,
                    subtitle = {
                        AnimatedVisibility(visible = !startDateIsToday) {
                            Text(stringResource(R.string.set_current_date_long_press))
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.CalendarToday, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timetable_start),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                ) {
                    AnimatedContent(
                        targetState = timetable.semesterStart.toDisplayString(),
                        label = "start_date"
                    ) { startDateStr ->
                        Text(
                            startDateStr, style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item { // 结束日期
                TitleCard(
                    onClick = onEndDateClick,
                    onLongClick = onEndDateLongClick,
                    subtitle = {
                        AnimatedVisibility(visible = timetable.semesterEnd != null) {
                            Text(stringResource(R.string.set_never_ends_long_press))
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.CalendarMonth, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_timetable_end),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                ) {
                    AnimatedContent(
                        targetState = timetable.semesterEnd?.toDisplayString()
                            ?: stringResource(R.string.never_ends),
                        label = "end_date"
                    ) { endDateStr ->
                        Text(
                            endDateStr, style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item { // 颜色
                ColorPickerCard(
                    label = stringResource(R.string.edit_timetable_color),
                    color = timetable.displayColor,
                    onClick = onColorClick,
                    onLongClick = onColorLongClick,
                    isNull = timetable.color == Color.Unspecified
                )
            }

            if (timetable.timetableId != 0L) { // 只有编辑时才显示
                item {
                    DeleteButton(
                        label = stringResource(R.string.edit_timetable_delete), onClick = onDelete
                    )
                }
            }
        }
    }
}