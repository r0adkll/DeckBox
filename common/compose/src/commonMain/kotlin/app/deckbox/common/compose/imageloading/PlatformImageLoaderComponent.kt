package app.deckbox.common.compose.imageloading

import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.LogPriority as HeartwoodLogPriority
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.logging.bark
import com.r0adkll.kotlininject.merge.annotations.ContributesTo
import com.seiko.imageloader.util.LogPriority
import com.seiko.imageloader.util.Logger

expect interface PlatformImageLoaderComponent

val DeckBoxImageLoaderLogger: Logger by lazy {
  object : Logger {

    override fun isLoggable(priority: LogPriority): Boolean = priority > LogPriority.DEBUG

    override fun log(priority: LogPriority, tag: String, data: Any?, throwable: Throwable?, message: String) {
      bark(
        priority = when (priority) {
          LogPriority.VERBOSE -> HeartwoodLogPriority.VERBOSE
          LogPriority.DEBUG -> HeartwoodLogPriority.DEBUG
          LogPriority.INFO -> HeartwoodLogPriority.INFO
          LogPriority.WARN -> HeartwoodLogPriority.WARN
          LogPriority.ERROR -> HeartwoodLogPriority.ERROR
          LogPriority.ASSERT -> HeartwoodLogPriority.ERROR
        },
        throwable = throwable,
        tag = tag,
        extras = data?.let { mapOf("data" to it.toString()) },
      ) {
        message
      }
    }
  }
}

@ContributesTo(MergeAppScope::class)
interface ImageLoaderComponent : PlatformImageLoaderComponent
