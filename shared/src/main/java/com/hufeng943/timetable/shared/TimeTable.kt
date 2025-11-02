package com.hufeng943.timetable.shared

// kotlinx.serialization
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder

// kotlinx.datetime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

import kotlin.time.Instant // kotlin2.2中datetime.Instant已经废弃

// kotlinx.serialization 还没原生支持 kotlin.time.Instant,先自定义个序列化器
object InstantAsLongSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("InstantAsLong", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeLong(value.toEpochMilliseconds())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.fromEpochMilliseconds(decoder.decodeLong())
    }
}

// ——————————模型层——————————

@Serializable
enum class WeekPattern { // 单双周课程
    EVERY_WEEK, ODD_WEEK, EVEN_WEEK
}

@Serializable
data class TimeSlot( // 课程时间
    val startTime: LocalTime,
    val endTime: LocalTime,
    val dayOfWeek: DayOfWeek
) {
    init {
        require(endTime > startTime) { "End time must be after start time." }
    }
}

@Serializable
data class Course( // 科目
    val id: Long = 0,
    val name: String,
    val timeSlots: List<TimeSlot> = emptyList(),
    val recurrence: WeekPattern = WeekPattern.EVERY_WEEK, // 默认每周重复
    val location: String?,
    val color: Int = 0xFF2196F3.toInt(), // 默认蓝色
    val teacher: String?
) {
    init {
        require(name.isNotBlank()) { "Course name cannot be blank." }
    }
}

@Serializable
data class TimeTable(
    val allCourses: List<Course>,
    val timeTableId: Long = 0,
    val semesterName: String, // 课程表的名称
    @Serializable(with = InstantAsLongSerializer::class)// 这是kotlin.time.Instant
    val createdAt: Instant,
    val semesterStart: LocalDate, // 课表开始日期
    val semesterEnd: LocalDate? // 课表结束日期,有可能永不结束
) {
    init {
        require(semesterName.isNotBlank()) { "Semester name cannot be blank." }
        require(semesterStart < (semesterEnd ?: LocalDate(9999, 12, 31))) { "Invalid semester dates." }
    }
    // 取课表开始当周的周一
    val semesterStartMonday: LocalDate by lazy {
        val offsetDays = (semesterStart.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
        semesterStart.minus(offsetDays.toLong(), DateTimeUnit.DAY)
    }
}

// ——————————UI层——————————
data class CourseUi(
    val id: Long,
    val name: String,
    val timeSlot: TimeSlot,
    val color: Int,
    val location: String?,
    val teacher: String?,
    val recurrence: WeekPattern
    ){
    init {
        require(name.isNotBlank()) { "CourseUI name cannot be blank." }
        require(timeSlot.endTime > timeSlot.startTime) { "End time must be after start time." }
    }
}

// ——————————转换——————————
// 取唯一ID
private fun generateCourseUiId(courseId: Long, slot: TimeSlot): Long =
    (courseId shl 14) or
            (slot.dayOfWeek.isoDayNumber.toLong() shl 11) or
            (slot.startTime.hour.toLong() shl 6) or
            slot.startTime.minute.toLong()
private fun getWeekIndex(date: LocalDate,timeTable: TimeTable): Int {
    val offsetDays = (date.dayOfWeek.isoDayNumber - DayOfWeek.MONDAY.isoDayNumber).mod(7)
    val currentMonday = date.minus(offsetDays.toLong(), DateTimeUnit.DAY)

    val daysBetween = currentMonday.toEpochDays() - timeTable.semesterStartMonday.toEpochDays()
    if (daysBetween < 0) return 0
    return (daysBetween / 7 + 1).toInt()
}

fun TimeTable.getWeekIndexForDate(date: LocalDate): Int {
    return getWeekIndex(date, this)
}

// 将单门课程全部时间段的 CourseUi 列表
fun Course.toUiCourses(): List<CourseUi> =
    timeSlots.map { slot ->
        CourseUi(
            id = generateCourseUiId(id, slot),
            name = name,
            timeSlot = slot,
            recurrence = recurrence,
            color = color,
            location = location,
            teacher = teacher
        )
    }.sortedWith(
        compareBy<CourseUi> { it.timeSlot.dayOfWeek.isoDayNumber } // 星期排序
            .thenBy { it.timeSlot.startTime }                     // 同一天按开始时间
    )

// 将整个课表全部时间段的 CourseUi 列表
fun TimeTable.toUiCourses(): List<CourseUi> =
    allCourses.flatMap { it.toUiCourses() }

// 单门课程当天的 CourseUi 列表
fun Course.toDayUiCourses(date: LocalDate, isOddWeek: Boolean): List<CourseUi> {
    // 过滤单双周
    if (recurrence == WeekPattern.ODD_WEEK && !isOddWeek) return emptyList()
    if (recurrence == WeekPattern.EVEN_WEEK && isOddWeek) return emptyList()

    return timeSlots
        .filter { it.dayOfWeek == date.dayOfWeek }  // 同一天
        .sortedBy { it.startTime }                 // 同一天按开始时间排序
        .map { slot ->
            CourseUi(
                id = generateCourseUiId(id, slot),
                name = name,
                timeSlot = slot,
                recurrence = recurrence,
                color = color,
                location = location,
                teacher = teacher
            )
        }
}

fun TimeTable.toDayUiCourses(date: LocalDate): List<CourseUi> {
    val weekIndex = getWeekIndexForDate(date)
    if (weekIndex == 0) return emptyList() // 课程未开始

    val isOddWeek = weekIndex % 2 != 0

    return allCourses
        .flatMap { course -> course.toDayUiCourses(date, isOddWeek) } // 复用 Course 层逻辑
        .sortedBy { it.timeSlot.startTime }
}

