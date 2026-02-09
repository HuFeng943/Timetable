package com.hufeng943.timetable.presentation.ui.screens.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.components.TimetableCard
import com.hufeng943.timetable.shared.ui.CourseUi

@Composable
fun CourseListPager(courseUis: List<CourseUi>) {
    val scrollState = rememberScalingLazyListState()

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(), state = scrollState, contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(
                        text = "课程安排", style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            items(
                items = courseUis, key = { it.timeSlot.id }) { courseUi ->
                TimetableCard(courseUi) {}
            }
        }
    }
}