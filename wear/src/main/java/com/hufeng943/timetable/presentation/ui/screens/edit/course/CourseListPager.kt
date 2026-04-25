package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.common.displayName
import com.hufeng943.timetable.shared.model.Course

@Composable
fun CourseListPager(
    courses: List<Course>,
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
                    CourseCard(
                        course = course,
                        onClick = { onCourseClick(course.id) },
                        onLongClick = { onCourseLongClick(course.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course, onClick: () -> Unit, onLongClick: () -> Unit, modifier: Modifier = Modifier
) {
    TitleCard(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier.fillMaxWidth(),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(course.color))
                )
                Spacer(Modifier.width(8.dp))
                Text(course.displayName, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        },
        subtitle = {
            // 组合地点和教师信息
            val info = listOfNotNull(
                course.location,
                course.teacher,
                stringResource(R.string.edit_course_number, course.timeSlots.size)
            ).joinToString(stringResource(R.string.info_separator))
            Text(
                info, maxLines = 1, modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE
                )
            )
        })
}