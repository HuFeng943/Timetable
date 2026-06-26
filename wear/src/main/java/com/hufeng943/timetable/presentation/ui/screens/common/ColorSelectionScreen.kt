package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight

@Composable
fun ColorSelectionScreen(onSave: (color: Color) -> Unit) {
    val haptic = LocalHapticFeedback.current
    val colorList = remember {
        listOf(
            0xFFE57373,
            0xFFF06292,
            0xFFBA68C8,
            0xFF9575CD,
            0xFF7986CB,
            0xFF64B5F6,
            0xFF4FC3F7,
            0xFF4DD0E1,
            0xFF4DB6AC,
            0xFF81C784,
            0xFFAED581,
            0xFFFFB74D
        ).map { Color(it) }
    }
    val scrollState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        TransformingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            flingBehavior = TransformingLazyColumnDefaults.snapFlingBehavior(scrollState),
            rotaryScrollableBehavior = RotaryScrollableDefaults.snapBehavior(scrollState),
            contentPadding = contentPadding
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ListHeaderDefaults.minimumTopListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text("选择颜色")
                }
            }

            // 颜色分组 每行 3 个
            val rows = colorList.chunked(3)
            items(rows.size) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    rows[rowIndex].forEach { color ->
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress) // 模拟按钮按下震动
                                onSave(color)
                            },
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(48.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = color),
                            transformation = SurfaceTransformation(transformationSpec),
                            contentPadding = PaddingValues(0.dp)
                        ) {}
                    }
                }
            }
        }
    }
}