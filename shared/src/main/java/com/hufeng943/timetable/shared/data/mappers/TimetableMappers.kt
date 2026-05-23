package com.hufeng943.timetable.shared.data.mappers

import com.hufeng943.timetable.shared.data.entities.TimetableEntity
import com.hufeng943.timetable.shared.data.relations.TimetableWithCourses
import com.hufeng943.timetable.shared.model.Timetable
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

fun TimetableWithCourses.toTimetable(): Timetable {
    val tableEntity = this.timetable

    // Long (毫秒) -> Instant
    val createdAt = Instant.fromEpochMilliseconds(tableEntity.createdAtMillis)

    // Long (Epoch Day) -> LocalDate
    val semesterStart = LocalDate.fromEpochDays(tableEntity.semesterStartEpochDay.toInt())
    val semesterEnd = tableEntity.semesterEndEpochDay?.toInt()?.let { LocalDate.fromEpochDays(it) }

    return Timetable(
        timetableId = tableEntity.id,
        semesterName = tableEntity.semesterName,
        createdAt = createdAt,
        semesterStart = semesterStart,
        semesterEnd = semesterEnd,
        allCourses = this.courses.map { it.toCourse() },
        color = tableEntity.color,
    )
}

fun Timetable.toTimetableEntity(): TimetableEntity = TimetableEntity(
    id = this.timetableId,
    semesterName = this.semesterName,
    createdAtMillis = this.createdAt.toEpochMilliseconds(),
    semesterStartEpochDay = this.semesterStart.toEpochDays(),
    semesterEndEpochDay = this.semesterEnd?.toEpochDays(),
    color = this.color
)