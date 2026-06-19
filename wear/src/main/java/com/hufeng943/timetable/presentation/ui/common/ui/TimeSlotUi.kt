package com.hufeng943.timetable.presentation.ui.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hufeng943.timetable.R
import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

data class TimeSlotUi(
    val id: Long,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
    val dayOfWeek: DayOfWeek?,
    val recurrence: WeekPattern,
    val remark: String?,
    val color: Long
) {
    val displayRemark: String
        @Composable
        get() = remark ?: stringResource(R.string.not_set)
}
