package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TitleCard
import com.hufeng943.timetable.R

@Composable
fun ColorPickerCard(
    label: String,
    color: Color,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isNull: Boolean
) {
    TitleCard(
        onClick = onClick,
        onLongClick = onLongClick,
        subtitle = { if (!isNull) Text(stringResource(R.string.clear_long_press)) },
        title = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )
        }
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.medium
                )
        )
    }
}