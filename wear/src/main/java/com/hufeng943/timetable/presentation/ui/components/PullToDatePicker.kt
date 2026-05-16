package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.material3.MaterialTheme
import kotlin.math.roundToInt

@Composable
fun rememberPullToDatePickerState(
    maxDragDistanceDp: Dp = 120.dp,
    refreshThresholdDp: Dp = 80.dp
): PullToDatePickerState {
    val density = LocalDensity.current
    val maxDragDistance = remember(density) { with(density) { maxDragDistanceDp.toPx() } }
    val refreshThreshold = remember(density) { with(density) { refreshThresholdDp.toPx() } }
    val dragAnimatable = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    return remember(maxDragDistance, refreshThreshold, scope) {
        PullToDatePickerState(dragAnimatable, maxDragDistance, refreshThreshold, scope)
    }
}

fun Modifier.pullToDatePickerDrag(state: PullToDatePickerState): Modifier = this.then(
    Modifier.pointerInput(Unit) {
        detectVerticalDragGestures(
            onVerticalDrag = { _, dragAmount ->
                val currentOffset = state.dragOffset
                if (dragAmount > 0) {
                    val newOffset = (currentOffset + dragAmount).coerceIn(0f, state.maxDragDistance)
                    state.snapTo(newOffset)
                } else if (dragAmount < 0 && currentOffset > 0) {
                    val newOffset = (currentOffset + dragAmount).coerceAtLeast(0f)
                    state.snapTo(newOffset)
                }
            },
            onDragEnd = { state.animateToTarget() }
        )
    }
)

@Composable
fun rememberPullToRefreshConnection(
    scrollState: ScalingLazyListState,
    state: PullToDatePickerState
): NestedScrollConnection {
    return remember(scrollState, state) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val firstVisibleItem = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()
                val isAtTop = firstVisibleItem?.index == 0 && (firstVisibleItem.offset) > -100
                val currentOffset = state.dragOffset

                if (available.y > 0 && isAtTop && currentOffset < state.maxDragDistance) {
                    val newOffset =
                        (currentOffset + available.y).coerceIn(0f, state.maxDragDistance)
                    val consumed = newOffset - currentOffset
                    state.snapTo(newOffset)
                    return Offset(0f, consumed)
                } else if (available.y < 0 && currentOffset > 0) {
                    val newOffset = (currentOffset + available.y).coerceAtLeast(0f)
                    val consumed = newOffset - currentOffset
                    state.snapTo(newOffset)
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                state.animateToTarget()
                return Velocity.Zero
            }
        }
    }
}

@Composable
fun PullToDatePicker(
    dragOffset: Float,
    refreshThreshold: Float,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
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
            }
        ) {
            content()
        }
    }
}
