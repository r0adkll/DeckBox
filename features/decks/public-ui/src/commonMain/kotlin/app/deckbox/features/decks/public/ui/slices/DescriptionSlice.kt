package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent

class DescriptionSlice : ComposeSlice {
  override val config: DeckCardSlice = DeckCardSlice.Description

  @Composable
  override fun ColumnScope.Content(
    deck: Deck,
    eventSink: (DeckCardEvent) -> Unit,
  ) {
    if (deck.description == null) return

    Text(
      text = deck.description!!,
      style = MaterialTheme.typography.bodyMedium,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .fillMaxWidth()
        .padding(
          horizontal = SlicePaddingHorizontal,
          vertical = SlicePaddingVertical,
        ),
    )
  }
}
