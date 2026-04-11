package com.hufeng943.timetable.shared.model

import kotlinx.serialization.Serializable

/**
 * 科目数据类，应该是每个科目都单独拥有的
 * 因为可以有多个timeSlot
 */
@Serializable
data class Course(
    val id: Long = 0,
    val name: String = "",
    val timeSlots: List<TimeSlot> = emptyList(),
    val location: String? = null,
    val color: Long = -1L,
    val teacher: String? = null
)