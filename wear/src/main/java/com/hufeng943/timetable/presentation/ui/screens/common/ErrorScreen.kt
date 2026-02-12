package com.hufeng943.timetable.presentation.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
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
    is AppError.TimetableNotFound -> "找不到这个课表 (ID: $id)"
    is AppError.CourseNotFound -> "找不到这节课 (ID: $id)"
    is AppError.TimeSlotNotFound -> "找不到这个时间段 (ID: $id)"
    is AppError.InvalidParameter -> "跳转参数错误：$navArgs"
    is AppError.Unknown -> "发生了未知的错误：${original.message}"
    else -> "发生了未知的错误"
}