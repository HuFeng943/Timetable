package com.hufeng943.timetable.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text

@Composable
fun DeleteButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    transformation: SurfaceTransformation? = null
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        transformation = transformation,
        icon = {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null
            )
        },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            iconColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Text(label)
    }
}