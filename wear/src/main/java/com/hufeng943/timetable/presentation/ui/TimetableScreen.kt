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

object TimeFormatters {
    val formatter24h: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    }
    val formatter12h: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("h:mm", Locale.getDefault())
    }
    val amPmFormatter: DateTimeFormatter by lazy {
        DateTimeFormatter.ofPattern("a", Locale.US)
    }
}


@Composable
fun TextTime(time: LocalTime) {
    val context = LocalContext.current
    val is24Hour = remember { DateFormat.is24HourFormat(context) }
    val localTime = remember(time) { java.time.LocalTime.of(time.hour, time.minute) }
    val aTextStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 9.sp,
        lineHeight = 9.sp
    )
    val timeStr = remember(localTime, is24Hour) {
        if (is24Hour) TimeFormatters.formatter24h.format(localTime)
        else TimeFormatters.formatter12h.format(localTime)
    }

    if (is24Hour) {
        Text(text = timeStr, style = MaterialTheme.typography.labelMedium)
    } else {
        val amPm = remember(localTime) { TimeFormatters.amPmFormatter.format(localTime) }
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
                text = amPm,
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