package com.hufeng943.timetable.presentation.ui.screens.edit.course

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
import com.hufeng943.timetable.shared.model.Course

@Composable
fun EditCourseMainPager(
    course: Course,
    onClickSave: () -> Unit,
    onClickName: () -> Unit,
    onClickLocation: () -> Unit,
    onLongClickLocation: () -> Unit,
    onClickTeacher: () -> Unit,
    onLongClickTeacher: () -> Unit,
    onClickColor: () -> Unit,
    onClickDelete: () -> Unit,
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(
            onClick = onClickSave
        ) {
            Icon(
                Icons.Default.Check, contentDescription = stringResource(R.string.check)
            )
        }
    }) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(
                        if (course.id == 0L) "添加课程"
                        else "编辑课程"
                    )
                }
            }

            item { // 课程名称
                TitleCard(
                    onClick = onClickName,
                    title = {
                        Text(
                            "课程名称", style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(course.name, style = MaterialTheme.typography.labelLarge)
                }
            }

            item { // 上课地点
                TitleCard(
                    onClick = onClickLocation,
                    onLongClick = onLongClickLocation,
                    subtitle = { if (course.location != null) Text("长按清除设置") },
                    title = {
                        Text(
                            "上课地点", style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(
                        course.location ?: "未设置", style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 授课教师
                TitleCard(
                    onClick = onClickTeacher,
                    onLongClick = onLongClickTeacher,
                    subtitle = { if (course.teacher != null) Text("长按清除设置") },
                    title = {
                        Text(
                            "授课教师", style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(
                        course.teacher ?: "未设置", style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 颜色
                ColorPickerCard(
                    label = "课程颜色", color = course.color, onClick = onClickColor
                )
            }

            if (course.id != 0L) {
                item {
                    DeleteButton(
                        label = "删除此课程", onClick = onClickDelete
                    )
                }
            }
        }
    }
}