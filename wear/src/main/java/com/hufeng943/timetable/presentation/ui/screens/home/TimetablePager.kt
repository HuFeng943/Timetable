package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.rotary.onPreRotaryScrollEvent
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
import com.hufeng943.timetable.presentation.ui.NavRoutes.courseDetail
import com.hufeng943.timetable.presentation.ui.common.LocalNavController
import com.hufeng943.timetable.presentation.ui.common.navigateSingle
import com.hufeng943.timetable.presentation.ui.common.ui.CourseUi
import com.hufeng943.timetable.presentation.ui.components.CourseCard
import com.hufeng943.timetable.presentation.ui.components.PullToDatePicker
import com.hufeng943.timetable.presentation.ui.components.PullToDatePickerState
import com.hufeng943.timetable.presentation.ui.components.pullToDatePickerDrag
import com.hufeng943.timetable.presentation.ui.components.rememberPullToDatePickerState
import com.hufeng943.timetable.presentation.ui.components.rememberPullToRefreshConnection
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.home.TimetableViewModel
import kotlinx.datetime.LocalDate

@Composable
fun TimetablePager(
    viewModel: TimetableViewModel = hiltViewModel(),
    onOpenStateChanged: (Boolean) -> Unit = {}
) {
    val uiState by viewModel.dateCoursesUi.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val navController = LocalNavController.current

    when (val state = uiState) {
        is UiState.Loading -> {
            LaunchedEffect(Unit) { onOpenStateChanged(false) } // 安全重置
            LoadingScreen()
        }

        is UiState.Empty -> {
            LaunchedEffect(Unit) { onOpenStateChanged(false) } // 安全重置
            EmptyPager()
        }

        is UiState.Error -> {
            LaunchedEffect(Unit) { onOpenStateChanged(false) } // 安全重置
            ErrorScreen(state.throwable)
        }

        is UiState.Success -> {
            val coursesUi = state.data
            val pullToDatePickerState = rememberPullToDatePickerState()

            val handleDateSelected: (LocalDate) -> Unit = { date ->
                viewModel.updateSelectedDate(date)
            }

            // 组件被拉出时通知上层锁定 Pager
            LaunchedEffect(pullToDatePickerState.dragOffset) {
                onOpenStateChanged(pullToDatePickerState.dragOffset > 0)
            }

            if (coursesUi.isEmpty()) {
                EmptyCoursePager(
                    state = pullToDatePickerState,
                    selectedDate = selectedDate,
                    onDateSelected = handleDateSelected
                )
            } else {
                CourseListPager(
                    coursesUi = coursesUi,
                    state = pullToDatePickerState,
                    itemKey = { courseUi -> courseUi.timeSlot.id },
                    selectedDate = selectedDate,
                    onDateSelected = handleDateSelected
                ) { courseUi ->
                    CourseCard(courseUi) {
                        navController.navigateSingle(courseDetail(courseUi.timeSlot.id))
                    }
                }
            }
        }
    }
}

/**
 * 空状态页面组件
 */
@Composable
private fun EmptyCoursePager(
    state: PullToDatePickerState,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    ScreenScaffold {
        PullToDatePicker(
            dragOffset = state.dragOffset,
            refreshThreshold = state.refreshThreshold,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .pullToDatePickerDrag(state),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_empty_course_hint),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * 课程列表页面组件
 */
@Composable
private fun CourseListPager(
    coursesUi: List<CourseUi>,
    state: PullToDatePickerState,
    itemKey: (CourseUi) -> Any,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (CourseUi) -> Unit
) {
    val scrollState = rememberScalingLazyListState(initialCenterItemIndex = 0)
    var isTouching by remember { mutableStateOf(false) }

    val nestedScrollConnection = rememberPullToRefreshConnection(
        scrollState = scrollState, state = state, isTouching = { isTouching })

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        PullToDatePicker(
            dragOffset = state.dragOffset,
            refreshThreshold = state.refreshThreshold,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        ) {
            ScalingLazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .onPreRotaryScrollEvent {
                        state.dragOffset > 0 // 直接消费掉表冠事件
                    }
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                isTouching = event.changes.any { it.pressed }
                            }
                        }
                    }
                    .nestedScroll(nestedScrollConnection),
                state = scrollState,
                contentPadding = contentPadding) {
                item {
                    ListHeader {
                        Text(
                            text = stringResource(R.string.home_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
                itemsIndexed(
                    items = coursesUi, key = { _, item -> itemKey(item) }) { _, item ->
                    itemContent(item)
                }
            }
        }
    }
}
