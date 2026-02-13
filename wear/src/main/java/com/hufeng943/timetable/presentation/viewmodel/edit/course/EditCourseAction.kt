package com.hufeng943.timetable.presentation.viewmodel.edit.course

import androidx.compose.ui.graphics.Color

sealed class EditCourseAction {
    data class UpdateName(val name: String) : EditCourseAction()
    data class UpdateLocation(val location: String? = null) : EditCourseAction()
    data class UpdateTeacher(val teacher: String? = null) : EditCourseAction()
    data class UpdateColor(val color: Color) : EditCourseAction()
    object Upsert : EditCourseAction()
    object Delete : EditCourseAction()
}