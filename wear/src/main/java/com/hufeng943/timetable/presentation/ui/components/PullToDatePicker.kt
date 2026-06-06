package com.hufeng943.timetable.presentation.ui.components

import HorizontalDatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.wear.compose.material3.Text
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun rememberPullToDatePickerState(
    maxDragDistanceDp: Dp = 120.dp, refreshThresholdDp: Dp = 110.dp
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
        detectVerticalDragGestures(onVerticalDrag = { _, dragAmount ->
            val currentOffset = state.dragOffset
            if (dragAmount > 0) {
                val newOffset = (currentOffset + dragAmount).coerceIn(0f, state.maxDragDistance)
                state.snapTo(newOffset)
            } else if (dragAmount < 0 && currentOffset > 0) {
                val newOffset = (currentOffset + dragAmount).coerceAtLeast(0f)
                state.snapTo(newOffset)
            }
        }, onDragEnd = { state.animateToTarget() })
    })

@Composable
fun rememberPullToRefreshConnection(
    scrollState: ScalingLazyListState, state: PullToDatePickerState, isTouching: () -> Boolean
): NestedScrollConnection {
    return remember(scrollState, state) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (!isTouching()) {
                    return Offset.Zero
                }
                val firstVisibleItem = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()
                val isAtTop = firstVisibleItem?.index == 0 && (firstVisibleItem.offset) > -80
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
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    content: @Composable () -> Unit
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = dragOffset > 0,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxWidth()
        ) {
            val progress = (dragOffset / refreshThreshold).coerceIn(0f, 1f)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, (dragOffset - 65.dp.toPx()).roundToInt()) }
                    .graphicsLayer { alpha = progress },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { onDateSelected(today) }
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "今日",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                HorizontalDatePicker(
                    selectedDate = selectedDate,
                    onDateSelected = onDateSelected
                )
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