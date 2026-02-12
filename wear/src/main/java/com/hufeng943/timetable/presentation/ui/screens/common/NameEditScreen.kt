package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R

@Suppress("AssignedValueIsNeverRead")
@Composable
fun NameEditScreen(
    label: String, initialText: String, onSave: (String) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    // 状态管理
    var textValue by remember { mutableStateOf(initialText) }

    ScreenScaffold(scrollState = scrollState, edgeButton = {
        EdgeButton(onClick = {
            onSave(textValue)
        }) {
            Icon(
                Icons.Default.Check, contentDescription = stringResource(R.string.check)
            )
        }
    }) { contentPadding ->
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            contentPadding = contentPadding
        ) {
            item {
                ListHeader {
                    Text(label)
                }
            }
            item {
                BasicTextField(
                    value = textValue,
                    onValueChange = {
                        // 过滤掉换行符
                        textValue = it.replace("\n", "")
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = CircleShape // 圆角两边
                        )
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}