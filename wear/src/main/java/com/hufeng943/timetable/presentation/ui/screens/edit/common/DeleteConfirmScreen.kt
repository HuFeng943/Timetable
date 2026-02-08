package com.hufeng943.timetable.presentation.ui.screens.edit.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text

@Composable
fun DeleteConfirm(
    detail: String, onConfirm: () -> Unit, onCancel: () -> Unit
) {
    val confirmScrollState = rememberScalingLazyListState()
    ScreenScaffold(scrollState = confirmScrollState) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = confirmScrollState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ListHeader {
                    Text(
                        "确定删除吗？",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            item {
                Text(
                    text = "$detail 一旦删除就找不回来了",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            item {
                // 取消
                FilledTonalButton(
                    onClick = onCancel, modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("我再想想")
                }
            }

            item {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("确认删除")
                }
            }
        }
    }
}