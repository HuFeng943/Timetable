package com.hufeng943.timetable.presentation.ui.screens.edit.course

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.datetime.LocalDate
import kotlin.time.Clock

class EditTimetableState(
    private val initialTimetable: Timetable?,
    private val today: LocalDate
) {
    // 基础状态
    var semesterName by mutableStateOf(initialTimetable?.semesterName ?: "课表")
    var semesterStartDate by mutableStateOf(initialTimetable?.semesterStart ?: today)
        private set
    var semesterEndDate by mutableStateOf<LocalDate?>(initialTimetable?.semesterEnd)
        private set
    var semesterColor by mutableStateOf(Color(initialTimetable?.color ?: 0xFFE57373))

    // 校验
    fun updateStartDate(newDate: LocalDate = today) {
        semesterStartDate = newDate
        semesterEndDate?.let { end ->
            if (end < semesterStartDate) {
                semesterEndDate = semesterStartDate
            }
        }
    }

    fun updateEndDate(newDate: LocalDate? = null) {
        semesterEndDate = newDate
        if (newDate != null && newDate < semesterStartDate) {
            semesterStartDate = newDate
        }
    }

    fun snapShot(): Timetable {
        return Timetable(
            timetableId = initialTimetable?.timetableId ?: 0,
            semesterName = semesterName.ifBlank { "未命名课表" },
            createdAt = initialTimetable?.createdAt ?: Clock.System.now(),
            semesterStart = semesterStartDate,
            semesterEnd = semesterEndDate,
            color = semesterColor.toArgb().toLong() and 0xFFFFFFFFL
        )
    }
}