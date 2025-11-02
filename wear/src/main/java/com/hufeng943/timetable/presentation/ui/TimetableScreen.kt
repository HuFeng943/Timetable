package com.hufeng943.timetable.presentation.ui

import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

import com.hufeng943.timetable.shared.*
import kotlinx.datetime.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimetableScreen(
    modifier: Modifier = Modifier,
    courses: List<CourseUi>,
    title: String,
    targetIndex: Int = 0
) {

    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = rememberScalingLazyListState(initialCenterItemIndex = targetIndex)
    ) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (courses.isEmpty()) {
            item {
                Text("今天没课，休息！", style = MaterialTheme.typography.bodyLarge)
            }
            return@ScalingLazyColumn
        }
        itemsIndexed(
            items = courses,
            key = { _, pair -> pair.id } // 用课程ID稳定key
        ) { _, pair ->
            TimeTableCard(pair)
        }

    }
}

@Composable
fun TextTime(time: LocalTime) {
    val context = LocalContext.current
    val is24Hour = remember { DateFormat.is24HourFormat(context) }  // Locale固定，不重查
    val localDateTime = remember(time) { java.time.LocalTime.of(time.hour, time.minute) }  // 缓存转换
    val pattern = if (is24Hour) "HH:mm" else "h:mm"
    val formatter1 = remember(pattern) {  // 缓存formatter
        DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    }

    val timeStr = remember(formatter1, localDateTime) { formatter1.format(localDateTime) }  // 缓存格式字符串

    val aTextStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 9.sp,
        lineHeight = 9.sp
    )

    if (is24Hour) {
        Text(
            text = timeStr,
            style = MaterialTheme.typography.labelMedium
        )
    } else {
        val formatter2 = remember {  // 缓存AM/PM formatter
            DateTimeFormatter.ofPattern("a", Locale.US)
        }
        val aStr = remember(formatter2, localDateTime) {
            formatter2.format(localDateTime)
        }  // 缓存AM/PM

        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = timeStr,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = aStr,
                style = aTextStyle
            )
        }
    }
}

@Composable
fun TimeTableCard(course: CourseUi){
    Card(
        onClick = {}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // 区域 1 时间
            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
                horizontalAlignment = Alignment.End, // 右对齐
                verticalArrangement = Arrangement.Top
            ) {
                TextTime(time = course.timeSlot.startTime)
                Spacer(modifier = Modifier.height(2.dp)) // 垂直间距
                TextTime(time = course.timeSlot.endTime)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(course.name, style = MaterialTheme.typography.titleMedium)
        }

    }
}