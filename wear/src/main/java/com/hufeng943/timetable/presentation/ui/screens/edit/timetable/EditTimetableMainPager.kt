package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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
                Icon(Icons.Default.Check, contentDescription = stringResource(R.string.check))
            }
        }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(
                        if (timetable.timetableId == 0L) stringResource(R.string.edit_timetable_add) else stringResource(
                            R.string.edit_timetable_edit
                        )
                    )
                }
            }

            item { // 修改名称
                TitleCard(
                    onClick = onNameClick,
                    title = {
                        Text(
                            stringResource(R.string.edit_timetable_name),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(
                        timetable.displayName, style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 开始日期
                TitleCard(
                    onClick = onStartDateClick,
                    onLongClick = onStartDateLongClick,
                    subtitle = { if (!startDateIsToday) Text(stringResource(R.string.set_current_date_long_press)) },
                    title = { Text(stringResource(R.string.edit_timetable_start)) },
                ) {
                    Text(
                        timetable.semesterStart.toDisplayString(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 结束日期
                TitleCard(
                    onClick = onEndDateClick,
                    onLongClick = onEndDateLongClick,
                    subtitle = { if (timetable.semesterEnd != null) Text(stringResource(R.string.set_never_ends_long_press)) },
                    title = { Text(stringResource(R.string.edit_timetable_end)) },
                ) {
                    Text(
                        timetable.semesterEnd?.toDisplayString()
                            ?: stringResource(R.string.never_ends),
                        style = MaterialTheme.typography.labelLarge
                    )
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
