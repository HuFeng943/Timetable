package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class PullToDatePickerState(
    val dragAnimatable: Animatable<Float, AnimationVector1D>,
    val maxDragDistance: Float,
    val refreshThreshold: Float,
    private val coroutineScope: CoroutineScope
) {
    val dragOffset: Float get() = dragAnimatable.value

    fun snapTo(value: Float) {
        coroutineScope.launch { dragAnimatable.snapTo(value) }
    }

    fun animateToTarget() {
        coroutineScope.launch {
            if (dragOffset > 0) {
                val targetValue = if (dragOffset >= refreshThreshold) refreshThreshold else 0f
                dragAnimatable.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        }
    }
}
