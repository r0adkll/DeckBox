package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import cafe.adriel.lyricist.LocalStrings
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExportHeaderSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Header.Export

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    CardHeader(
      title = {
        Text(
          text = deck.name.ifBlank { LocalStrings.current.deckDefaultNoName },
          style = MaterialTheme.typography.titleLarge,
        )
      },
      subtitle = {
        Text(LocalStrings.current.deckLastUpdated(deck.updatedAt.readableFormat))
      },
      trailing = {
        IconButton(
          onClick = { eventSink(DeckCardEvent.Export) },
        ) {
          Icon(Icons.Rounded.Share, contentDescription = null)
        }
      },
    )
  }
}

class ThumbnailHeaderSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Header.Thumbnail

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    CardHeader(
      title = {
        Text(
          text = deck.name.ifBlank { LocalStrings.current.deckDefaultNoName },
          style = MaterialTheme.typography.titleLarge,
        )
      },
      subtitle = {
        Text(LocalStrings.current.deckLastUpdated(deck.updatedAt.readableFormat))
      },
      trailing = {
        IconButton(
          onClick = { eventSink(DeckCardEvent.Export) },
        ) {
          Icon(Icons.Rounded.Share, contentDescription = null)
        }
      },
    )
  }
}

val LocalDateTime.readableFormat: String
  get() {
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    val hourAdjusted = hour % 12
    val amPM = if (hour < 12) "AM" else "PM"
    return if (now.date == date) {
      "$hourAdjusted:$minute $amPM"
    } else {
      "${month.name} ${dayOfMonth}, $year $hourAdjusted:$minute $amPM"
    }
  }
