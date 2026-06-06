package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import java.time.format.TextStyle
import kotlin.math.roundToInt
import kotlin.time.Clock

@Composable
fun rememberPullToDatePickerState(
    maxDragDistanceDp: Dp = 120.dp, refreshThresholdDp: Dp = 80.dp
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
fun HorizontalDatePicker(
    selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit, modifier: Modifier = Modifier
) {
    val dates = remember {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        (-7..7).map { today.plus(it, DateTimeUnit.DAY) }
    }

    // 自动定位到当前选中日期的索引
    val selectedIndex = remember(selectedDate) { dates.indexOf(selectedDate).coerceAtLeast(0) }
    val listState = rememberLazyListState()

    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 2) {
            listState.scrollToItem(selectedIndex - 2)
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(dates) { date ->
            val isSelected = date == selectedDate
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            }
            val contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            Box(
                modifier = Modifier
                    .size(width = 46.dp, height = 56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .clickable { onDateSelected(date) }, contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = date.dayOfWeek.toDisplayString(TextStyle.SHORT),
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = date.day.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
fun PullToDatePicker(
    dragOffset: Float, refreshThreshold: Float, selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    content: @Composable () -> Unit

) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (dragOffset > 0) {
            val progress = (dragOffset / refreshThreshold).coerceIn(0f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, (dragOffset - 40.dp.toPx()).roundToInt()) },
                contentAlignment = Alignment.Center
            ) {
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