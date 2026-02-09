package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.LocalNavController
import com.hufeng943.timetable.presentation.ui.NavRoutes.courseDetail
import com.hufeng943.timetable.presentation.ui.components.TimetableCard
import com.hufeng943.timetable.presentation.ui.screens.loading.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.TimetableViewModel
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.shared.ui.mappers.toCourseUi


@Composable
fun TimetablePager(
    viewModel: TimetableViewModel = hiltViewModel(), targetIndex: Int = 0
) {
    val uiState by viewModel.currentTableUi.collectAsState()
    // EmptyPager() 没有课表才
    val navController = LocalNavController.current
    when (val state = uiState) {
        UiState.Loading -> LoadingScreen()
        UiState.Empty -> EmptyPager()
        is UiState.Success -> {
            val (timetable, coursesIdList) = state.data
            if (coursesIdList.isEmpty()) {
                ScreenScaffold {// 有课表但没有课！
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.home_empty_course_hint),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            } else {

                val scrollState = rememberScalingLazyListState(initialCenterItemIndex = targetIndex)
                val sortedCourses = remember(coursesIdList) {
                    coursesIdList.sortedWith(compareBy { timetable.toCourseUi(it)!!.timeSlot.startTime })
                }

                ScreenScaffold(scrollState = scrollState) { contentPadding ->
                    ScalingLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = scrollState,
                        contentPadding = contentPadding
                    ) {
                        item {
                            ListHeader {
                                Text(
                                    text = stringResource(R.string.home_title),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        itemsIndexed(
                            items = sortedCourses,
                            key = { _, item -> "${item.courseId}-${item.timeSlotId}" } // 唯一 key
                        ) { dailyOrderIndex, idPair ->
                            val course =
                                timetable.toCourseUi(idPair)?.copy(dailyOrder = dailyOrderIndex + 1)
                            if (course != null) {
                                TimetableCard(course) {
                                    // 传递两ID
                                    navController.navigate(courseDetail(idPair.timeSlotId))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
