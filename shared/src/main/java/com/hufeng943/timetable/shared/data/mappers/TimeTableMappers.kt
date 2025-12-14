package com.hufeng943.timetable.shared.data.mappers

import com.hufeng943.timetable.shared.data.entities.TimeTableEntity
import com.hufeng943.timetable.shared.data.relations.TimeTableWithCourses
import com.hufeng943.timetable.shared.model.TimeTable
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

fun TimeTableWithCourses.toTimeTable(): TimeTable {
    val tableEntity = this.timeTable

    // Long (毫秒) -> Instant
    val createdAt = Instant.fromEpochMilliseconds(tableEntity.createdAtMillis)

    // Long (Epoch Day) -> LocalDate
    val semesterStart = LocalDate.fromEpochDays(tableEntity.semesterStartEpochDay.toInt())
    val semesterEnd = tableEntity.semesterEndEpochDay?.toInt()?.let { LocalDate.fromEpochDays(it) }

    return TimeTable(
        timeTableId = tableEntity.id,
        semesterName = tableEntity.semesterName,
        createdAt = createdAt,
        semesterStart = semesterStart,
        semesterEnd = semesterEnd,
        allCourses = this.courses.map { it.toCourse() })
}

fun TimeTable.toTimeTableEntity(): TimeTableEntity = TimeTableEntity(
    id = this.timeTableId,
    semesterName = this.semesterName,
    createdAtMillis = this.createdAt.toEpochMilliseconds(),
    semesterStartEpochDay = this.semesterStart.toEpochDays(),
    semesterEndEpochDay = this.semesterEnd?.toEpochDays()
)