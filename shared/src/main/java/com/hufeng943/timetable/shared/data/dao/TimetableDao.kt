package com.hufeng943.timetable.shared.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.data.entities.TimetableEntity
import com.hufeng943.timetable.shared.data.relations.TimetableWithCourses
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    // ------更新------
    @Upsert
    suspend fun upsertTimetable(timetable: TimetableEntity): Long

    @Upsert
    suspend fun upsertCourse(course: CourseEntity): Long

    @Upsert
    suspend fun upsertTimeSlot(slot: TimeSlotEntity): Long

    // ------删除------
    @Delete
    suspend fun deleteTimetable(timetable: TimetableEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Delete
    suspend fun deleteTimeSlot(timeSlot: TimeSlotEntity)

    // 按 ID 删除
    @Query("DELETE FROM time_tables WHERE id = :id")
    suspend fun deleteTimetableById(id: Long)

    @Query("DELETE FROM courses WHERE id = :courseId")
    suspend fun deleteCourseById(courseId: Long)

    @Query("DELETE FROM time_slots WHERE id = :timeSlotId")
    suspend fun deleteTimeSlotById(timeSlotId: Long)

    // ------查询-------

    // 查询所有课表
    @Transaction
    @Query("SELECT * FROM time_tables")
    fun getAllTimetablesWithCourses(): Flow<List<TimetableWithCourses>>

}