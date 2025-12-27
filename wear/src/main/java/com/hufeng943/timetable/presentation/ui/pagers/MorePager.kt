package com.hufeng943.timetable.presentation.ui.pagers

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.wear.compose.material.Chip

@Composable
fun MorePager() {
    // 原生 Wear Compose 的状态管理
    val scrollState = rememberScalingLazyListState()

    ScreenScaffold(scrollState = scrollState) {
        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                ListHeader {
                    Text("设置")
                }
            }

            item {
                Chip(
                    onClick = { },
                    label = { Text("编辑课程表", maxLines = 1) },
                    secondaryLabel = { Text("对的，编辑", maxLines = 1) },
                    icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}