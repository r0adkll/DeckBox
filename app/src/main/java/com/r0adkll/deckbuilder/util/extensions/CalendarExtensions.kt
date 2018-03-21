package com.r0adkll.deckbuilder.util.extensions

import java.util.*


fun Date.toCalendar(): Calendar {
    val c = Calendar.getInstance()
    c.time = this
    return c
}


fun Calendar.clearTime(): Calendar {
    val current = Calendar.getInstance()
    val year = current[Calendar.YEAR]
    val month = current[Calendar.MONTH]
    val day = current[Calendar.DAY_OF_MONTH]

    current.clear()
    current[Calendar.YEAR] = year
    current[Calendar.MONTH] = month
    current[Calendar.DAY_OF_MONTH] = day

    return current
}


fun Calendar.setDate(year: Int, month: Int, dayOfMonth: Int): Calendar {
    this.clearTime()
    this[Calendar.YEAR] = year
    this[Calendar.MONTH] = month
    this[Calendar.DAY_OF_MONTH] = dayOfMonth
    return this
}