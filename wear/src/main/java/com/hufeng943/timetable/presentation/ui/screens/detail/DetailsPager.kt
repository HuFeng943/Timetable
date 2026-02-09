package com.hufeng943.timetable.presentation.ui.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.shared.ui.CourseUi

@Composable
fun DetailsPager(courseUi: CourseUi) {
    ScreenScaffold {
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
}