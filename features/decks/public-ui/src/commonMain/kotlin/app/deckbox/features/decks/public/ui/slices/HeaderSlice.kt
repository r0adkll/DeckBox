package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.outline.Export
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.core.extensions.readableFormat
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import cafe.adriel.lyricist.LocalStrings

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
          Icon(DeckBoxIcons.Outline.Export, contentDescription = null)
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
