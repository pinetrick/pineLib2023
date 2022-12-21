package com.pine.lib.addone.datetime

import java.util.*

class DateTime {
    private val calendar: Calendar = Calendar.getInstance()

    constructor() {
        // Do nothing, use current time as default
    }

    constructor(year: Int, month: Int, day: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
    }

    constructor(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
    }

    val year: Int
        get() = calendar.get(Calendar.YEAR)

    val month: Int
        get() = calendar.get(Calendar.MONTH) + 1

    val day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)

    val hour: Int
        get() = calendar.get(Calendar.HOUR_OF_DAY)

    val minute: Int
        get() = calendar.get(Calendar.MINUTE)

    val second: Int
        get() = calendar.get(Calendar.SECOND)

    val dayOfWeek: DayOfWeek
        get() = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            Calendar.SATURDAY -> DayOfWeek.SATURDAY
            else -> DayOfWeek.SUNDAY
        }

    enum class DayOfWeek {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    fun addDays(days: Int): DateTime {
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return this
    }

    fun addMonths(months: Int): DateTime {
        calendar.add(Calendar.MONTH, months)
        return this
    }

    fun addYears(years: Int): DateTime {
        calendar.add(Calendar.YEAR, years)
        return this
    }

    fun addHours(hours: Int): DateTime {
        calendar.add(Calendar.HOUR_OF_DAY, hours)
        return this
    }

    fun addMinutes(minutes: Int): DateTime {
        calendar.add(Calendar.MINUTE, minutes)
        return this
    }

    fun addSeconds(seconds: Int): DateTime {
        calendar.add(Calendar.SECOND, seconds)
        return this
    }

    override fun toString(): String {
        return "$year-$month-$day $hour:$minute:$second"
    }

    companion object {
        val now: DateTime
            get() = DateTime()
    }
}
