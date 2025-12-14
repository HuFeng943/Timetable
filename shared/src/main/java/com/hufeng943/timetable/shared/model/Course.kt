package com.hufeng943.timetable.shared.model

import kotlinx.serialization.Serializable

/**
 * 科目数据类，应该是每个科目都单独拥有的
 * 因为可以有多个timeSlot
 */
@Serializable
data class Course(
    val id: Long,
    val name: String,
    val timeSlots: List<TimeSlot> = emptyList(),
    val location: String? = null,
    val color: Long = 0xFF2196F3, // 默认蓝色
    val teacher: String? = null
) {
    init {
        require(name.isNotBlank()) { "Course name cannot be blank." }
    }
}