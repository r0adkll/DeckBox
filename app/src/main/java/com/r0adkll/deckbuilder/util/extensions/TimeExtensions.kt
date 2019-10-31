package com.r0adkll.deckbuilder.util.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.clear()
    cal.timeInMillis = this
    return cal
}

fun Long.isToday(): Boolean = toCalendar().isToday()

fun Long.get(field: Int): Int = this.toCalendar().get(field)

fun Calendar.isToday(): Boolean {
    val current = Calendar.getInstance()
    return current.get(Calendar.YEAR) == this.get(Calendar.YEAR) &&
            current.get(Calendar.DAY_OF_YEAR) == this.get(Calendar.DAY_OF_YEAR)
}

fun Calendar.isYesterday(): Boolean {
    val current = Calendar.getInstance()
    current.add(Calendar.DAY_OF_YEAR, -1)
    return current.get(Calendar.YEAR) == this.get(Calendar.YEAR) &&
            current.get(Calendar.DAY_OF_YEAR) == this.get(Calendar.DAY_OF_YEAR)
}

fun Calendar.isThisWeek(): Boolean {
    val current = Calendar.getInstance()
    return current.get(Calendar.YEAR) == this.get(Calendar.YEAR) &&
            current.get(Calendar.WEEK_OF_YEAR) == this.get(Calendar.WEEK_OF_YEAR)
}

fun Calendar.isThisYear(): Boolean {
    val current = Calendar.getInstance()
    return current.get(Calendar.YEAR) == this.get(Calendar.YEAR)
}

fun Date.dateOfBirth(): String {
    val f = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
    return f.format(this)
}
