package com.hufeng943.timetable.presentation.ui.screens.edit.timeslot

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
import com.hufeng943.timetable.presentation.ui.common.LocalAppConfig
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import com.hufeng943.timetable.presentation.ui.components.DeleteButton
import com.hufeng943.timetable.shared.model.TimeSlot
import kotlinx.datetime.toJavaDayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EditTimeSlotMainPager(
    timeSlot: TimeSlot,
    onSave: () -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
    onDayOfWeekClick: () -> Unit,
    onRecurrenceClick: () -> Unit,
    onRemarkClick: () -> Unit,
    onRemarkLongClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val config = LocalAppConfig.current
    val scrollState = rememberScalingLazyListState()

    ScreenScaffold(
        scrollState = scrollState,
        edgeButton = {
            EdgeButton(onClick = onSave) {
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
                    Text(if (timeSlot.id == 0L) "添加时间段" else "编辑时间段")
                }
            }

            // 开始时间
            item {
                TitleCard(
                    onClick = onStartTimeClick,
                    title = { Text("开始时间", style = MaterialTheme.typography.labelLarge) }
                ) {
                    Text(timeSlot.startTime?.toDisplayString(config.is24HourFormat) ?: "未设置")
                }
            }

            // 结束时间
            item {
                TitleCard(
                    onClick = onEndTimeClick,
                    title = { Text("结束时间", style = MaterialTheme.typography.labelLarge) }
                ) {
                    Text(timeSlot.endTime?.toDisplayString(config.is24HourFormat) ?: "未设置")
                }
            }

            // 星期
            item {
                TitleCard(
                    onClick = onDayOfWeekClick,
                    title = { Text("星期", style = MaterialTheme.typography.labelLarge) }
                ) {
                    Text(
                        text = timeSlot.dayOfWeek?.toJavaDayOfWeek()
                            ?.getDisplayName(TextStyle.FULL, Locale.getDefault()) ?: "未设置",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            // 重复规则
            item {
                TitleCard(
                    onClick = onRecurrenceClick,
                    title = { Text("重复规则", style = MaterialTheme.typography.labelLarge) }
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
                    subtitle = { if (timeSlot.remark != null) Text("长按清除") },
                    title = { Text("备注", style = MaterialTheme.typography.labelLarge) }
                ) {
                    Text(
                        text = timeSlot.remark ?: "未设置",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            if (timeSlot.id != 0L) {
                item {
                    DeleteButton(label = "删除此时间段", onClick = onDelete)
                }
            }
        }
    }
}