package com.r0adkll.deckbuilder.util.extensions

import com.google.gson.internal.bind.util.ISO8601Utils
import java.text.ParsePosition
import java.text.SimpleDateFormat
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


fun String.iso8601(): Long {
    return ISO8601Utils.parse(this, ParsePosition(0)).time
}