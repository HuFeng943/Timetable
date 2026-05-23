package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.ui.CourseEditUi
import com.hufeng943.timetable.presentation.ui.components.edit.EditCourseCard

@Composable
fun CourseListPager(
    courses: List<CourseEditUi>,
    onAddCourse: () -> Unit,
    onCourseClick: (courseId: Long) -> Unit,
    onCourseLongClick: (courseId: Long) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(onClick = onAddCourse) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.edit_course_add)
            )
        }
    }) { contentPadding ->
        ScalingLazyColumn(
            autoCentering = null,
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(stringResource(R.string.edit_course_title))
                }
            }
            if (courses.isEmpty()) {
                item {
                    Text(stringResource(R.string.edit_course_empty))
                }
            } else {
                items(courses, key = { it.id }) { course ->
                    EditCourseCard(
                        course = course,
                        onClick = { onCourseClick(course.id) },
                        onLongClick = { onCourseLongClick(course.id) },
                    )
                }
            }
        }
    }
}
