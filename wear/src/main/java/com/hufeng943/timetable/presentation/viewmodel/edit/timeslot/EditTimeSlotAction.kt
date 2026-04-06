package com.hufeng943.timetable.presentation.viewmodel.edit.timeslot

import com.hufeng943.timetable.shared.model.WeekPattern
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalTime

sealed class EditTimeSlotAction {
    data class UpdateStartTime(val startTime: LocalTime) : EditTimeSlotAction()
    data class UpdateEndTime(val endTime: LocalTime) : EditTimeSlotAction()
    data class UpdateDayOfWeek(val dayOfWeek: DayOfWeek) : EditTimeSlotAction()
    data class UpdateRecurrence(val recurrence: WeekPattern) : EditTimeSlotAction()
    data class UpdateRemark(val remark: String?) : EditTimeSlotAction()
    object Upsert : EditTimeSlotAction()
    object Delete : EditTimeSlotAction()
}