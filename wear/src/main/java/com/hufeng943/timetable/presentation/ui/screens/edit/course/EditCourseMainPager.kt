package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi
import com.hufeng943.timetable.presentation.ui.components.ColorPickerCard
import com.hufeng943.timetable.presentation.ui.components.DeleteButton

@Composable
fun EditCourseMainPager(
    course: CourseUi,
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
    val scrollState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(
        scrollState = scrollState,
        edgeButton = {
            EdgeButton(onClick = onSave) {
                Icon(
                    Icons.Rounded.Check, contentDescription = stringResource(R.string.check)
                )
            }
        }
    ) { contentPadding ->
        TransformingLazyColumn(
            state = scrollState,
            contentPadding = contentPadding
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ListHeaderDefaults.minimumTopListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    AnimatedContent(targetState = course.id == 0L, label = "header_text") { isAdd ->
                        Text(
                            if (isAdd) stringResource(R.string.edit_course_add)
                            else stringResource(R.string.edit_course_edit)
                        )
                    }
                }
            }

            item { // 课程名称
                TitleCard(
                    onClick = onNameClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Book, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_course_name),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                ) {
                    AnimatedContent(
                        targetState = course.displayName,
                        label = "course_name"
                    ) { name ->
                        Text(name, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            item { // 上课地点
                TitleCard(
                    onClick = onLocationClick,
                    onLongClick = onLocationLongClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec),
                    subtitle = {
                        AnimatedVisibility(visible = course.location != null) {
                            Text(stringResource(R.string.clear_long_press))
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Place, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_course_location),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                ) {
                    AnimatedContent(
                        targetState = course.displayLocation,
                        label = "course_location"
                    ) { location ->
                        Text(
                            location,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item { // 授课教师
                TitleCard(
                    onClick = onTeacherClick,
                    onLongClick = onTeacherLongClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec),
                    subtitle = {
                        AnimatedVisibility(visible = course.teacher != null) {
                            Text(stringResource(R.string.clear_long_press))
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Person, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                stringResource(R.string.edit_course_teacher),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    },
                ) {
                    AnimatedContent(
                        targetState = course.displayTeacher,
                        label = "course_teacher"
                    ) { teacher ->
                        Text(
                            teacher,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            item { // 颜色
                ColorPickerCard(
                    label = stringResource(R.string.edit_course_color),
                    color = course.displayColor,
                    onClick = onColorClick,
                    onLongClick = onColorLongClick,
                    isNull = course.color == Color.Unspecified,
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }

            if (course.id != 0L) {
                item {
                    DeleteButton(
                        label = stringResource(R.string.edit_course_delete),
                        onClick = onDelete,
                        modifier = Modifier
                            .fillMaxWidth()
                            .transformedHeight(this, transformationSpec)
                            .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                        transformation = SurfaceTransformation(transformationSpec)
                    )
                }
            }
        }
    }
}