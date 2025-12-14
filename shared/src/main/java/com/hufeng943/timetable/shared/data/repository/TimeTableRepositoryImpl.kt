package com.hufeng943.timetable.shared.data.repository

import com.hufeng943.timetable.shared.data.dao.TimeTableDao
import com.hufeng943.timetable.shared.data.mappers.toCourseEntity
import com.hufeng943.timetable.shared.data.mappers.toTimeSlotEntity
import com.hufeng943.timetable.shared.data.mappers.toTimeTable
import com.hufeng943.timetable.shared.data.mappers.toTimeTableEntity
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimeTableRepositoryImpl(private val dao: TimeTableDao) : TimeTableRepository {
    override suspend fun insertTimeTable(timeTable: TimeTable): Long {
        val timeTableEntity = timeTable.toTimeTableEntity()
        return dao.insertTimeTable(timeTableEntity)
    }

    override suspend fun insertCourse(course: Course, timeTableId: Long): Long {
        val courseEntity = course.toCourseEntity(timeTableId)
        return dao.insertCourse(courseEntity)
    }

    override suspend fun insertTimeSlot(courseId: Long, timeSlot: TimeSlot): Long {
        val timeSlotEntity = timeSlot.toTimeSlotEntity(courseId)
        return dao.insertTimeSlot(timeSlotEntity)
    }

    override suspend fun deleteTimeTable(timeTableId: Long) {
        dao.deleteTimeTableById(timeTableId)
    }

    override suspend fun deleteCourse(courseId: Long) {
        dao.deleteCourseById(courseId)
    }

    override suspend fun deleteTimeSlot(timeSlotId: Long) {
        dao.deleteTimeSlotById(timeSlotId)
    }

    override fun getAllTimeTables(): Flow<List<TimeTable>> {
        return dao.getAllTimeTablesWithCourses()
            .map { relationsList ->
                relationsList.map { relation ->
                    relation.toTimeTable()
                }
            }
    }
}