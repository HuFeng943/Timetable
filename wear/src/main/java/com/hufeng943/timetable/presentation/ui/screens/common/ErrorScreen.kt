package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.viewmodel.AppError

@Composable
fun ErrorScreen(
    throwable: Throwable,
) {
    ScreenScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.apperror),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = throwable.asMessage(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Throwable.asMessage(): String = when (this) {
    is AppError.TimetableNotFound -> stringResource(
        R.string.apperror_timetablenotfound,
        id ?: stringResource(R.string.unknown)
    )

    is AppError.CourseNotFound -> stringResource(
        R.string.apperror_coursenotfound,
        id ?: stringResource(R.string.unknown)
    )

    is AppError.TimeSlotNotFound -> stringResource(
        R.string.apperror_timeslotnotfound,
        id ?: stringResource(R.string.unknown)
    )

    is AppError.InvalidParameter -> stringResource(R.string.apperror_invalidparameter, navArgs)
    AppError.UnexpectedEmpty() -> stringResource(R.string.apperror_unexpectedempty)
    is AppError.Unknown -> stringResource(
        R.string.apperror_unknown, original.message ?: stringResource(R.string.unknown)
    )

    else -> this.message ?: stringResource(R.string.apperror_unknown, this.javaClass.simpleName)
}