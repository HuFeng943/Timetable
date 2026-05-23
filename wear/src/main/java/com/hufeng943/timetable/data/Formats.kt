package com.hufeng943.timetable.data

import kotlinx.datetime.DayOfWeek

enum class TimeFormat {
    SYSTEM,
    H12,
    H24
}

enum class FirstDayOfTheWeek(val dayOfWeek: DayOfWeek?) {
    SYSTEM(null),
    MONDAY(DayOfWeek.MONDAY),
    SUNDAY(DayOfWeek.SUNDAY),
    SATURDAY(DayOfWeek.SATURDAY);

    companion object {
        fun fromDayOfWeek(dayOfWeek: DayOfWeek?): FirstDayOfTheWeek {
            return when (dayOfWeek) {
                DayOfWeek.MONDAY -> MONDAY
                DayOfWeek.SUNDAY -> SUNDAY
                DayOfWeek.SATURDAY -> SATURDAY
                else -> SYSTEM
            }
        }
    }
}