package app.deckbox.common.compose.extensions

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val LocalDateTime.timeAgo: String
  @Composable get() {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val nowMs = now.toInstant(TimeZone.UTC).toEpochMilliseconds()
    val thisMs = toInstant(TimeZone.UTC).toEpochMilliseconds()
    val elapsedMs = nowMs - thisMs
    val elapsedDuration = elapsedMs.milliseconds

    return when {
      elapsedDuration < 5.minutes -> LocalStrings.current.timeAgoNow
      elapsedDuration < 1.hours -> LocalStrings.current.timeAgoMinutes((elapsedDuration.inWholeMinutes % 60).toInt())
      elapsedDuration < 1.days -> LocalStrings.current.timeAgoHours((elapsedDuration.inWholeHours % 24).toInt())
      elapsedDuration < 30.days -> LocalStrings.current.timeAgoDays(elapsedDuration.inWholeDays.toInt())
      elapsedDuration < 365.days -> LocalStrings.current.timeAgoMonths((elapsedDuration.inWholeDays / 30).toInt())
      else -> LocalStrings.current.timeAgoYears((elapsedDuration.inWholeDays / 365).toInt())
    }
  }
