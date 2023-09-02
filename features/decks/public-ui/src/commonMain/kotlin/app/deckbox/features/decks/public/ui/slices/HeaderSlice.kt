package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Deck
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.outline.BoosterPack
import app.deckbox.common.compose.icons.outline.Decks
import app.deckbox.common.compose.icons.outline.Export
import app.deckbox.common.compose.widgets.CardHeader
import app.deckbox.common.compose.widgets.TonalIcon
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
      leading = {
        TonalIcon(
          DeckBoxIcons.Outline.Decks,
          contentDescription = null,
          modifier = Modifier.padding(top = 4.dp),
        )
      },
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
