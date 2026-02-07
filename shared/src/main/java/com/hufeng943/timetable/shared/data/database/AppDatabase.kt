package com.hufeng943.timetable.shared.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hufeng943.timetable.shared.data.dao.TimetableDao
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.data.entities.TimetableEntity

@Database(
    entities = [
        TimetableEntity::class,
        CourseEntity::class,
        TimeSlotEntity::class
    ],
    version = 3,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
}