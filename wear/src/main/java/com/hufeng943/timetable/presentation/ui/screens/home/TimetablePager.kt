package com.hufeng943.timetable.presentation.ui.screens.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.home_empty_course_hint),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    var dragOffset by remember { mutableFloatStateOf(0f) }
                    val animatable = remember { Animatable(0f) }
                    val scope = rememberCoroutineScope()
                    val maxDragDistance = with(LocalDensity.current) { 120.dp.toPx() }
                    val refreshThreshold = with(LocalDensity.current) { 80.dp.toPx() }

                    // 同步动画值和实际偏移
                    LaunchedEffect(animatable.value) {
                        dragOffset = animatable.value
                    }

                    // 创建嵌套滚动连接 - 在列表顶部时拦截向下滚动
                    val nestedScrollConnection = remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: androidx.compose.ui.geometry.Offset,
                                source: NestedScrollSource
                            ): androidx.compose.ui.geometry.Offset {
                                val firstVisibleItem =
                                    scrollState.layoutInfo.visibleItemsInfo.firstOrNull()
                                val isAtTop =
                                    firstVisibleItem?.index == 0 && (firstVisibleItem?.offset
                                        ?: 0) > -100
                                Log.d(
                                    "isAtTop",
                                    "${firstVisibleItem?.index} ${firstVisibleItem?.offset}"
                                )
                                // 向下滚动且列表在顶部时，增加下拉偏移
                                if (available.y > 0 && isAtTop && dragOffset < maxDragDistance) {
                                    val newOffset =
                                        (dragOffset + available.y).coerceIn(0f, maxDragDistance)
                                    val consumed = newOffset - dragOffset

                                    scope.launch {
                                        animatable.snapTo(newOffset)
                                    }

                                    return androidx.compose.ui.geometry.Offset(0f, consumed)
                                }
                                // 向上滚动且有偏移时，减少偏移（手动拉回）
                                else if (available.y < 0 && dragOffset > 0) {
                                    val newOffset = (dragOffset + available.y).coerceAtLeast(0f)
                                    val consumed = newOffset - dragOffset

                                    scope.launch {
                                        animatable.snapTo(newOffset)
                                    }

                                    return androidx.compose.ui.geometry.Offset(0f, consumed)
                                }
                                return androidx.compose.ui.geometry.Offset.Zero
                            }

                            override suspend fun onPostFling(
                                consumed: Velocity,
                                available: Velocity
                            ): Velocity {
                                // 手势结束后，直接回到顶部或刷新位置（不使用弹簧动画）
                                if (dragOffset > 0) {
                                    if (dragOffset >= refreshThreshold) {
                                        // 超过阈值，保持在下拉位置
                                        animatable.snapTo(refreshThreshold)
                                    } else {
                                        // 未超过阈值，直接回到顶部
                                        animatable.snapTo(0f)
                                    }
                                }

                                return Velocity.Zero
                            }
                        }
                    }

                    PullToDatePicker(
                        dragOffset = dragOffset,
                        refreshThreshold = refreshThreshold,
                        maxDragDistance = maxDragDistance,
                        onDragOffsetChange = { newOffset ->
                            scope.launch {
                                animatable.snapTo(newOffset)
                            }
                        }
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

@Composable
fun PullToDatePicker(
    dragOffset: Float,
    refreshThreshold: Float,
    maxDragDistance: Float,
    onDragOffsetChange: (Float) -> Unit,
    content: @Composable () -> Unit
) {
    Box {
        // 显示刷新指示器
        if (dragOffset > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(0, (dragOffset - 60.dp.toPx()).roundToInt())
                    }, contentAlignment = Alignment.Center
            ) {
                Text("下拉刷新")
            }
        }

        // 主要内容
        Box(
            modifier = Modifier.offset {
                IntOffset(0, dragOffset.roundToInt())
            }) {
            content()
        }
    }
}
