package com.hufeng943.timetable.shared.data.repository

import com.hufeng943.timetable.shared.data.dao.TimetableDao
import com.hufeng943.timetable.shared.data.mappers.toCourse
import com.hufeng943.timetable.shared.data.mappers.toCourseEntity
import com.hufeng943.timetable.shared.data.mappers.toTimeSlot
import com.hufeng943.timetable.shared.data.mappers.toTimeSlotEntity
import com.hufeng943.timetable.shared.data.mappers.toTimetable
import com.hufeng943.timetable.shared.data.mappers.toTimetableEntity
import com.hufeng943.timetable.shared.model.Course
import com.hufeng943.timetable.shared.model.TimeSlot
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimetableRepositoryImpl(private val dao: TimetableDao) : TimetableRepository {
    override suspend fun upsertTimetable(timetable: Timetable): Long {
        val timetableEntity = timetable.toTimetableEntity()
        return dao.upsertTimetable(timetableEntity)
    }

    override suspend fun upsertCourse(course: Course, timetableId: Long): Long {
        val courseEntity = course.toCourseEntity(timetableId)
        return dao.upsertCourse(courseEntity)
    }

    override suspend fun upsertTimeSlot(courseId: Long, timeSlot: TimeSlot): Long {
        val timeSlotEntity = timeSlot.toTimeSlotEntity(courseId)
        return dao.upsertTimeSlot(timeSlotEntity)
    }

    override suspend fun deleteTimetable(timetableId: Long) {
        dao.deleteTimetableById(timetableId)
    }

    override suspend fun deleteCourse(courseId: Long) {
        dao.deleteCourseById(courseId)
    }

    override suspend fun deleteTimeSlot(timeSlotId: Long) {
        dao.deleteTimeSlotById(timeSlotId)
    }

    override fun getAllTimetables(): Flow<List<Timetable>> {
        return dao.getTimetables().map { entities ->
            entities.map { it.toTimetable() }
        }
    }

    override fun getTimetableById(timetableId: Long): Flow<Timetable?> {
        return dao.getTimetableById(timetableId).map { it?.toTimetable() }
    }

    override fun getCourseById(courseId: Long): Flow<Course?> {
        return dao.getCourseById(courseId).map { it?.toCourse() }
    }

    override fun getTimeSlotById(timeSlotId: Long): Flow<TimeSlot?> {
        return dao.getTimeSlotById(timeSlotId).map { it?.toTimeSlot() }
    }

    override fun getCourseByTimeSlotId(timeSlotId: Long): Flow<Course?> {
        return dao.getCourseByTimeSlotId(timeSlotId).map { it?.toCourse() }
    }

    override fun getTimetableByCourseId(courseId: Long): Flow<Timetable?> {
        return dao.getTimetableByCourseId(courseId).map { it?.toTimetable() }
    }
}