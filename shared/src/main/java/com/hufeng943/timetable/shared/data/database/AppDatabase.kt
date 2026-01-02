package com.hufeng943.timetable.shared.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hufeng943.timetable.shared.data.dao.TimeTableDao
import com.hufeng943.timetable.shared.data.entities.CourseEntity
import com.hufeng943.timetable.shared.data.entities.TimeSlotEntity
import com.hufeng943.timetable.shared.data.entities.TimeTableEntity

@Database(
    entities = [
        TimeTableEntity::class,
        CourseEntity::class,
        TimeSlotEntity::class
    ],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timeTableDao(): TimeTableDao
}