package com.hufeng943.timetable.presentation.viewmodel

import com.hufeng943.timetable.shared.model.Timetable

sealed class TableAction {
    data class Upsert(val table: Timetable) : TableAction()
    data class Delete(val timetableId: Long) : TableAction()
    // TODO ......
}