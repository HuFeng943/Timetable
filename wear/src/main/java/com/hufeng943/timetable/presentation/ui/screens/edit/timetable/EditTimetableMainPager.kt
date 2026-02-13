package com.hufeng943.timetable.presentation.ui.screens.edit.timetable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
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
import com.hufeng943.timetable.presentation.ui.components.ColorPickerCard
import com.hufeng943.timetable.presentation.ui.components.DeleteButton
import com.hufeng943.timetable.shared.model.Timetable


@Composable
fun EditTimetableMainPager(
    timetable: Timetable,
    onClickSave: () -> Unit,
    onClickName: () -> Unit,
    onClickStartDate: () -> Unit,
    onLongClickStartDate: () -> Unit,
    onClickEndDate: () -> Unit,
    onLongClickEndDate: () -> Unit,
    onClickColor: () -> Unit,
    onClickDelete: () -> Unit,
    startDateIsToday: Boolean
) {
    val scrollState = rememberScalingLazyListState()

    ScreenScaffold(
        scrollState = scrollState, edgeButton = {
            EdgeButton(onClick = onClickSave) {
                Icon(Icons.Default.Check, contentDescription = stringResource(R.string.check))
            }
        }) { contentPadding ->
        ScalingLazyColumn(state = scrollState, contentPadding = contentPadding) {
            item {
                ListHeader {
                    Text(if (timetable.timetableId == 0L) stringResource(R.string.edit_timetable_add) else "编辑课表")
                }
            }

            item { // 修改名称
                TitleCard(
                    onClick = onClickName,
                    title = {
                        Text(
                            stringResource(R.string.edit_timetable_name),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(timetable.semesterName, style = MaterialTheme.typography.labelLarge)
                }
            }

            item { // 开始日期
                TitleCard(
                    onClick = onClickStartDate,
                    onLongClick = onLongClickStartDate,
                    subtitle = { if (!startDateIsToday) Text("长按设置为当前日期") },
                    title = { Text(stringResource(R.string.edit_timetable_start)) },
                ) {
                    Text(
                        timetable.semesterStart.toString(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 结束日期
                TitleCard(
                    onClick = onClickEndDate,
                    onLongClick = onLongClickEndDate,
                    subtitle = { if (timetable.semesterEnd != null) Text("长按设置为永不结束") },
                    title = { Text(stringResource(R.string.edit_timetable_end)) },
                ) {
                    Text(
                        timetable.semesterEnd?.toString() ?: "永不结束",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 颜色
                ColorPickerCard(
                    label = stringResource(R.string.edit_timetable_color),
                    color = timetable.color,
                    onClick = onClickColor
                )
            }

            if (timetable.timetableId != 0L) { // 只有编辑时才显示
                item {
                    DeleteButton(label = "删除此课表", onClick = onClickDelete)
                }
            }
        }
    }
}