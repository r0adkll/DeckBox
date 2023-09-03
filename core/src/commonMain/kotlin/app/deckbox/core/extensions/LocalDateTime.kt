package app.deckbox.core.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val LocalDateTime.readableFormat: String
  get() {
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    val hourAdjusted = hour % 12
    val amPM = if (hour < 12) "AM" else "PM"
    return if (now.date == date) {
      "$hourAdjusted:$minute $amPM"
    } else {
      "${month.name.capitalized()} $dayOfMonth, $year $hourAdjusted:${minute.withSignificantZero()} $amPM"
    }
  }

private fun Int.withSignificantZero(): String = if (this < 10) "0$this" else "$this"
