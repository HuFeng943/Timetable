package com.hufeng943.timetable.shared.data.repository

import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.coroutines.flow.Flow

interface TimeTableRepository {
    // 插入
    suspend fun insertTimeTable(timeTable: TimeTable): Long
    suspend fun insertCourse(course: Course, timeTableId: Long): Long
    suspend fun insertTimeSlot(courseId: Long, timeSlot: TimeSlot): Long

    // 删除课表
    suspend fun deleteTimeTable(timeTableId: Long)
    suspend fun deleteCourse(courseId: Long)
    suspend fun deleteTimeSlot(timeSlotId: Long)

    // 获取所有课表
    fun getAllTimeTables(): Flow<List<TimeTable>>
}