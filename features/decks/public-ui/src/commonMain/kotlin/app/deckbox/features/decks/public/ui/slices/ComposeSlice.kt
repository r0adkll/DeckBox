package app.deckbox.features.decks.public.ui.slices

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardSlice
import app.deckbox.features.decks.public.ui.events.DeckCardEvent

internal val SlicePaddingHorizontal = 16.dp
internal val SlicePaddingVertical = 8.dp

interface ComposeSlice {
  val config: DeckCardSlice

  @Composable
  fun ColumnScope.Content(deck: Deck, eventSink: (DeckCardEvent) -> Unit)
}
