package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.hufeng943.timetable.R
import com.hufeng943.timetable.presentation.viewmodel.AppError

@Composable
fun ErrorScreen(
    throwable: Throwable,
) {
    ScreenScaffold {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(throwable.asMessage())
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

    else -> stringResource(R.string.apperror_unknown, this)
}