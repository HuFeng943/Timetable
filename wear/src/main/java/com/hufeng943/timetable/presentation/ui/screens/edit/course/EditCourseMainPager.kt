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
import com.hufeng943.timetable.presentation.ui.common.displayName
import com.hufeng943.timetable.presentation.ui.common.toColor
import com.hufeng943.timetable.presentation.ui.components.ColorPickerCard
import com.hufeng943.timetable.presentation.ui.components.DeleteButton
import com.hufeng943.timetable.shared.model.Course

@Composable
fun EditCourseMainPager(
    course: Course,
    onSave: () -> Unit,
    onNameClick: () -> Unit,
    onLocationClick: () -> Unit,
    onLocationLongClick: () -> Unit,
    onTeacherClick: () -> Unit,
    onTeacherLongClick: () -> Unit,
    onColorClick: () -> Unit,
    onColorLongClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(
            onClick = onSave
        ) {
            Icon(
                Icons.Default.Check, contentDescription = stringResource(R.string.check)
            )
        }
    }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null, state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(
                        if (course.id == 0L) stringResource(R.string.edit_course_add)
                        else stringResource(R.string.edit_course_edit)
                    )
                }
            }

            item { // 课程名称
                TitleCard(
                    onClick = onNameClick,
                    title = {
                        Text(
                            stringResource(R.string.edit_course_name),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(course.displayName, style = MaterialTheme.typography.labelLarge)
                }
            }

            item { // 上课地点
                TitleCard(
                    onClick = onLocationClick,
                    onLongClick = onLocationLongClick,
                    subtitle = { if (course.location != null) Text(stringResource(R.string.clear_long_press)) },
                    title = {
                        Text(
                            stringResource(R.string.edit_course_location),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(
                        course.location ?: stringResource(R.string.not_set),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 授课教师
                TitleCard(
                    onClick = onTeacherClick,
                    onLongClick = onTeacherLongClick,
                    subtitle = { if (course.teacher != null) Text(stringResource(R.string.clear_long_press)) },
                    title = {
                        Text(
                            stringResource(R.string.edit_course_teacher),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                ) {
                    Text(
                        course.teacher ?: stringResource(R.string.not_set),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            item { // 颜色
                ColorPickerCard(
                    label = stringResource(R.string.edit_course_color),
                    color = course.color.toColor(),
                    onClick = onColorClick,
                    onLongClick = onColorLongClick,
                    isNull = course.color == -1L
                )
            }

            if (course.id != 0L) {
                item {
                    DeleteButton(
                        label = stringResource(R.string.edit_course_delete), onClick = onDelete
                    )
                }
            }
        }
    }
}