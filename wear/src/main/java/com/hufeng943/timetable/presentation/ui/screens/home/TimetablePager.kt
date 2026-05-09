package com.hufeng943.timetable.presentation.ui.screens.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
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
import com.hufeng943.timetable.presentation.ui.components.CourseCard
import com.hufeng943.timetable.presentation.ui.screens.common.ErrorScreen
import com.hufeng943.timetable.presentation.ui.screens.common.LoadingScreen
import com.hufeng943.timetable.presentation.viewmodel.UiState
import com.hufeng943.timetable.presentation.viewmodel.home.TimetableViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TimetablePager(
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val uiState by viewModel.dateCoursesUi.collectAsState()
    val navController = LocalNavController.current

    when (val state = uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Empty -> EmptyPager()
        is UiState.Error -> ErrorScreen(state.throwable)
        is UiState.Success -> {
            val coursesUi = state.data
            val scrollState = rememberScalingLazyListState(initialCenterItemIndex = 0)

            ScreenScaffold(scrollState = scrollState) { contentPadding ->
                if (coursesUi.isEmpty()) {
                    EmptyCourseHint()
                } else {
                    val scope = rememberCoroutineScope()
                    val dragAnimatable = remember { Animatable(0f) }

                    val maxDragDistance = with(LocalDensity.current) { 120.dp.toPx() }
                    val refreshThreshold = with(LocalDensity.current) { 80.dp.toPx() }

                    // 获取封装好的嵌套滚动连接
                    val nestedScrollConnection = rememberPullToRefreshConnection(
                        scrollState = scrollState,
                        dragAnimatable = dragAnimatable,
                        maxDragDistance = maxDragDistance,
                        refreshThreshold = refreshThreshold,
                        coroutineScope = scope
                    )

                    PullToDatePicker(
                        dragOffset = dragAnimatable.value, refreshThreshold = refreshThreshold
                    ) {
                        ScalingLazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(nestedScrollConnection),
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
                                key = { _, courseUi -> courseUi.timeSlot.id }) { _, courseUi ->
                                CourseCard(courseUi) {
                                    navController.navigateSingle(courseDetail(courseUi.timeSlot.id))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 下拉刷新的手势拦截与动画处理逻辑
 */
@Composable
private fun rememberPullToRefreshConnection(
    scrollState: ScalingLazyListState,
    dragAnimatable: Animatable<Float, AnimationVector1D>,
    maxDragDistance: Float,
    refreshThreshold: Float,
    coroutineScope: CoroutineScope
): NestedScrollConnection {
    return remember(scrollState, maxDragDistance, refreshThreshold) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val firstVisibleItem = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()
                val isAtTop = firstVisibleItem?.index == 0 && (firstVisibleItem.offset) > -100
                val currentOffset = dragAnimatable.value

                // 向下滚动且列表在顶部时，增加下拉偏移
                if (available.y > 0 && isAtTop && currentOffset < maxDragDistance) {
                    val newOffset = (currentOffset + available.y).coerceIn(0f, maxDragDistance)
                    val consumed = newOffset - currentOffset
                    coroutineScope.launch { dragAnimatable.snapTo(newOffset) }
                    return Offset(0f, consumed)
                }
                // 向上滚动且有偏移时，减少偏移（手动拉回）
                else if (available.y < 0 && currentOffset > 0) {
                    val newOffset = (currentOffset + available.y).coerceAtLeast(0f)
                    val consumed = newOffset - currentOffset
                    coroutineScope.launch { dragAnimatable.snapTo(newOffset) }
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val currentOffset = dragAnimatable.value
                // 手势结束后，使用动画回到顶部或维持在刷新位置
                if (currentOffset > 0) {
                    val targetValue =
                        if (currentOffset >= refreshThreshold) refreshThreshold else 0f
                    dragAnimatable.animateTo(
                        targetValue = targetValue, animationSpec = tween(durationMillis = 300)
                    )
                }
                return Velocity.Zero
            }
        }
    }
}

@Composable
private fun EmptyCourseHint() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.home_empty_course_hint),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun PullToDatePicker(
    dragOffset: Float, refreshThreshold: Float, content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 背景颜色变换
        if (dragOffset > 0) {
            val progress = (dragOffset / refreshThreshold).coerceIn(0f, 1f)
            val backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.2f + progress * 0.4f
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, (dragOffset - 60.dp.toPx()).roundToInt()) },
                contentAlignment = Alignment.Center
            ) {
                //TODO 添加日期选择器
            }
        }

        Box(
            modifier = Modifier.offset {
                IntOffset(0, dragOffset.roundToInt())
            }) {
            content()
        }
    }
}