package com.hufeng943.timetable.shared.data.repository

import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    // 更新
    suspend fun upsertTimetable(timetable: Timetable): Long
    suspend fun upsertCourse(course: Course, timetableId: Long): Long
    suspend fun upsertTimeSlot(courseId: Long, timeSlot: TimeSlot): Long

    // 删除课表
    suspend fun deleteTimetable(timetableId: Long)
    suspend fun deleteCourse(courseId: Long)
    suspend fun deleteTimeSlot(timeSlotId: Long)

    // 获取所有课表
    fun getAllTimetables(): Flow<List<Timetable>>
    fun getTimetableById(timetableId: Long): Flow<Timetable?>
    fun getCourseById(courseId: Long): Flow<Course?>
    fun getTimeSlotById(timeSlotId: Long): Flow<TimeSlot?>
    fun getCourseByTimeSlotId(timeSlotId: Long): Flow<Course?>
    fun getTimetableByCourseId(courseId: Long): Flow<Timetable?>
}