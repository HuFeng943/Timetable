package com.hufeng943.timetable.presentation.ui.screens.edit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.contract.TableAction
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

@Composable
fun EditTimeTable(
    timeTables: List<TimeTable>,
    onAction: (TableAction) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.edit_course_header_title))
                }
            }
            if (timeTables.isEmpty()) {
                item {
                    Text("还没有课表")
                }
            } else {
                items(timeTables, key = { it.timeTableId }) { timeTable ->
                    TitleCard(
                        onClick = { /* TODO: 切换当前课表或编辑详情 */ },
                        title = { Text(timeTable.semesterName) },
                        subtitle = {
                            Text(
                                text = "共 ${timeTable.allCourses.size} 门课程",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            val newTable = TimeTable(// TODO 测试
                semesterName = "2026 春季学期",
                createdAt = Clock.System.now(),
                semesterStart = LocalDate(2026, 2, 24),
                color = 0xFF669DF6,
                allCourses = emptyList()
            )
            item {
                // 专门的添加按钮，放在列表最后
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onAction(TableAction.Add(newTable)) },
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("新增课表")
                    }
                }
            }
        }
    }
}