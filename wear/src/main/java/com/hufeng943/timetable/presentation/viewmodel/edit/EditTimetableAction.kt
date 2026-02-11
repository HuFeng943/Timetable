package com.hufeng943.timetable.presentation.viewmodel.edit

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate

sealed class EditTimetableAction {
    data class UpdateName(val name: String) : EditTimetableAction()
    data class UpdateStartDate(val date: LocalDate) : EditTimetableAction()
    data class UpdateEndDate(val date: LocalDate? = null) : EditTimetableAction()
    data class UpdateColor(val color: Color) : EditTimetableAction()
    object Upsert : EditTimetableAction()
    object Delete : EditTimetableAction()
}