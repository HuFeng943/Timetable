package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.shared.model.Course

@Composable
fun CourseListPager(//TODO
    courses: List<Course>
) {
    LocalNavController.current
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(
            onClick = { }) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "新增课程"
            )
        }
    }) { contentPadding ->
        ScalingLazyColumn(
            state = scrollState, modifier = Modifier.fillMaxSize(), contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text("课程列表")
                }
            }
            if (courses.isEmpty()) {
                item {
                    Text("暂无课程")
                }
            } else {
                items(courses, key = { it.id }) { course ->
                    TitleCard(
                        onClick = { },
                        onLongClick = { },
                        title = { Text(course.name) },
                        subtitle = {
                            Text("${course.timeSlots.size} 个时间段")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}