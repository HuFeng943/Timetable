package com.hufeng943.timetable.presentation.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseUi
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi

@Composable
fun CourseDetailScreen(timeTable: TimeTable?, courseWithSlotId: CourseWithSlotId?) {
    val courseId = if (timeTable != null && courseWithSlotId != null) {
        timeTable.toCourseUi(
            CourseWithSlotId(
                courseWithSlotId.courseId, courseWithSlotId.timeSlotId
            )
        )
    } else null
    if (courseId != null) {
        HorizontalPager(
            modifier = Modifier.fillMaxSize(), state = rememberPagerState { 2 }) { page ->
            when (page) {
                0 -> DetailsScreen(courseId)
                1 -> CourseListScreen(courseId)
            }
        }
    } else Text(text = "未找到课程数据", color = Color.Red)
}

@Composable
fun DetailsScreen(courseUi: CourseUi) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = courseUi.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(text = "教师：${courseUi.teacher ?: "无"}")
            Text(text = "地点：${courseUi.location ?: "未填写"}")
            Text(
                text = "时间：${
                    courseUi.timeSlot.dayOfWeek.name.substring(
                        0, 3
                    )
                } ${courseUi.timeSlot.startTime} - ${courseUi.timeSlot.endTime}"
            )
            Text(text = "重复：${courseUi.timeSlot.recurrence}")
        }
    }
}

@Composable
fun CourseListScreen(courseUi: CourseUi) {
    //TODO
}