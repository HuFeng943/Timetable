package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.components.TimeTableCard
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseWithSlotId
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi


@Composable
fun TimetablePager(
    timeTable: TimeTable,
    coursesIdList: List<CourseWithSlotId>,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    title: String,
    targetIndex: Int = 0
) {
    when {
        coursesIdList.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = stringResource(R.string.home_empty_course_hint),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        else -> {
            val scrollState = rememberScalingLazyListState(initialCenterItemIndex = targetIndex)
            val sortedCourses = remember(coursesIdList) {
                coursesIdList.sortedWith(compareBy { timeTable.toCourseUi(it)!!.timeSlot.startTime })
            }

            ScreenScaffold(scrollState = scrollState) {
                ScalingLazyColumn(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = scrollState
                ) {
                    item {
                        Text(
                            text = title, style = MaterialTheme.typography.titleMedium
                        )
                    }
                    itemsIndexed(
                        items = sortedCourses,
                        key = { _, item -> "${item.courseId}-${item.timeSlotId}" } // 唯一 key
                    ) { dailyOrderIndex, idPair ->
                        val course =
                            timeTable.toCourseUi(idPair)?.copy(dailyOrder = dailyOrderIndex + 1)
                        if (course != null) {
                            TimeTableCard(course) {
                                // 传递两ID
                                navController.navigate("course_detail/${idPair.courseId}/${idPair.timeSlotId}")
                            }
                        }
                    }

                }
            }
        }
    }
}
