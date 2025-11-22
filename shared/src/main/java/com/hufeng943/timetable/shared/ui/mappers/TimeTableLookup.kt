package com.hufeng943.timetable.shared.ui.mappers

import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import com.hufeng943.timetable.shared.ui.CourseUi
import com.hufeng943.timetable.shared.ui.CourseWithSlotId

/**
 * 通过timeSlotId查找对应的对象
 * @return TimeSlot 查找失败则返回 null
 */
fun TimeTable.findTimeSlotById(timeSlotId: Long): TimeSlot? = this.timeSlotsMap[timeSlotId]
/**
 * 通过CourseWithSlotId反查CourseUi
 * @return CourseUi 查找失败则返回 null
 */
fun TimeTable.toCourseUi(courseWithSlotId: CourseWithSlotId): CourseUi? {
    val course = this.courseMap[courseWithSlotId.courseId]
    val timeSlot = this.timeSlotsMap[courseWithSlotId.timeSlotId]

    // 没找到？
    if (course == null || timeSlot == null) return null

    return CourseUi(
        name = course.name,
        timeSlot = timeSlot,
        color = course.color,
        location = course.location,
        teacher = course.teacher
    )
}