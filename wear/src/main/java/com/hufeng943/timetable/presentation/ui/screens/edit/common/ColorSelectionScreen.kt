package com.hufeng943.timetable.presentation.ui.screens.edit.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text

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
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ListHeader { Text("选择颜色") }
            }

            // 颜色分组 每行 3 个
            val rows = colorList.chunked(3)
            items(rows.size) { rowIndex ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    rows[rowIndex].forEach { color ->
                        Box(// 圆形按钮效果
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(
                                    48.dp
                                )
                                .background(color, CircleShape)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)// 模拟按钮按下震动
                                    onSave(color)
                                })
                    }
                }
            }
        }
    }
}

