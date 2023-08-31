package app.deckbox.core.time

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface FatherTime {

  fun now(): LocalDateTime
  fun nowInEpochMillis(): Long
}

object GrandFatherTime : FatherTime {

  override fun now(): LocalDateTime {
    return Clock.System.now()
      .toLocalDateTime(TimeZone.currentSystemDefault())
  }

  override fun nowInEpochMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
  }
}
