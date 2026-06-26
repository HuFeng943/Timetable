package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListHeaderDefaults
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.ui.components.toDisplayString
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

    val scrollState = rememberTransformingLazyColumnState(
        initialAnchorItemIndex = initialIndex
    )
    val transformationSpec = rememberTransformationSpec()

    ScreenScaffold(scrollState = scrollState) { contentPadding ->
        TransformingLazyColumn(
            state = scrollState,
            contentPadding = contentPadding,
            flingBehavior = TransformingLazyColumnDefaults.snapFlingBehavior(scrollState),
            rotaryScrollableBehavior = RotaryScrollableDefaults.snapBehavior(scrollState),
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup()
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ListHeaderDefaults.minimumTopListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text(stringResource(R.string.recurrence_title))
                }
            }

            items(patterns) { pattern ->
                RadioButton(
                    selected = (pattern == initialPattern),
                    onSelect = { onPatternSelected(pattern) },
                    label = { Text(pattern.toDisplayString()) },
                    secondaryLabel = {
                        when (pattern) {
                            WeekPattern.ODD_WEEK -> Text(stringResource(R.string.recurrence_odd_week))
                            WeekPattern.EVEN_WEEK -> Text(stringResource(R.string.recurrence_even_week))
                            else -> null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec)
                        .minimumVerticalContentPadding(ButtonDefaults.minimumVerticalListContentPadding),
                    transformation = SurfaceTransformation(transformationSpec)
                )
            }
        }
    }
}