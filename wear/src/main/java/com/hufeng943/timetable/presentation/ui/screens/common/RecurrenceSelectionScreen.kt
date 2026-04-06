package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.presentation.ui.common.toDisplayString
import com.hufeng943.timetable.shared.model.WeekPattern

@Composable
fun RecurrenceSelectionScreen(
    initialPattern: WeekPattern, onPatternSelected: (WeekPattern) -> Unit
) {
    val patterns = remember { WeekPattern.entries }

    val initialIndex = remember(patterns, initialPattern) {
        patterns.indexOf(initialPattern).coerceAtLeast(0) + 1
        // 补偿 Header 占用的位置
    }

    val scrollState = rememberScalingLazyListState(
        initialCenterItemIndex = initialIndex
    )
    ScalingLazyColumn(
        state = scrollState, modifier = Modifier
            .fillMaxSize()
            .selectableGroup()
    ) {
        item {
            ListHeader {
                Text("重复规则")
            }
        }

        items(patterns) { pattern ->
            RadioButton(
                selected = (pattern == initialPattern),
                onSelect = { onPatternSelected(pattern) },
                label = { Text(pattern.toDisplayString()) },
                secondaryLabel = {
                    when (pattern) {
                        WeekPattern.ODD_WEEK -> Text("仅在单周上课")
                        WeekPattern.EVEN_WEEK -> Text("仅在双周上课")
                        else -> null
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}