package com.hufeng943.timetable.shared.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.data.entities.TimeTableEntity
import com.hufeng943.timetable.shared.data.relations.TimeTableWithCourses
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTableDao {
    // ------插入------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTable(timeTable: TimeTableEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSlot(slot: TimeSlotEntity): Long

    // ------删除------
    @Delete
    suspend fun deleteTimeTable(timeTable: TimeTableEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Delete
    suspend fun deleteTimeSlot(timeSlot: TimeSlotEntity)

    // 按 ID 删除
    @Query("DELETE FROM time_tables WHERE id = :id")
    suspend fun deleteTimeTableById(id: Long)

    @Query("DELETE FROM courses WHERE id = :courseId")
    suspend fun deleteCourseById(courseId: Long)

    @Query("DELETE FROM time_slots WHERE id = :timeSlotId")
    suspend fun deleteTimeSlotById(timeSlotId: Long)

    // ------查询-------

    // 查询所有课表
    @Transaction
    @Query("SELECT * FROM time_tables")
    fun getAllTimeTablesWithCourses(): Flow<List<TimeTableWithCourses>>

}