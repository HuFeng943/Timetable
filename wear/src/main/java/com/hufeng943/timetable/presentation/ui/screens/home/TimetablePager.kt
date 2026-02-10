package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


@Composable
fun TimetablePager(
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val uiState by viewModel.dateCoursesUi.collectAsState()
    // EmptyPager() 没有课表才
    val navController = LocalNavController.current
    when (val state = uiState) {
        UiState.Loading -> LoadingScreen()
        UiState.Empty -> EmptyPager()
        is UiState.Success -> {
            val coursesUi = state.data
            if (coursesUi.isEmpty()) {
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
                val scrollState = rememberScalingLazyListState(initialCenterItemIndex = 0)

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
                            items = coursesUi,
                            key = { _, courseUi -> courseUi.timeSlot.id } // 唯一 key
                        ) { _, courseUi ->
                            TimetableCard(courseUi) {
                                navController.navigate(courseDetail(courseUi.timeSlot.id))
                            }
                        }
                    }
                }
            }
        }
    }
}

