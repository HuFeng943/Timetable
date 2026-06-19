package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.common.DynamicSubTheme
import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi


@Composable
fun CourseCard(course: CourseUi, onClick: () -> Unit) {
    DynamicSubTheme(seedColor = course.color) {
    Card(
        onClick = onClick,
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .heightIn(min = 48.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            // 区域 1 时间
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .fillMaxHeight()
                    .padding(top = 3.dp),
                horizontalAlignment = Alignment.End, // 右对齐
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TimeText(time = course.timeSlot.startTime)
                TimeText(time = course.timeSlot.endTime)
            }
            // 区域 2 名称
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = course.displayName,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center // 水平文字居中
            )
            // 区域 3 节次
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = course.dailyOrder.toString(),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
    }
}
