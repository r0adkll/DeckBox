package app.deckbox.features.decks.public.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.deckbox.common.compose.widgets.Tag
import app.deckbox.common.compose.widgets.TagStyle
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.core.settings.DeckCardSlice.*
import app.deckbox.features.decks.public.ui.events.DeckCardEvent
import app.deckbox.features.decks.public.ui.slices.ActionSlice
import app.deckbox.features.decks.public.ui.slices.DescriptionSlice
import app.deckbox.features.decks.public.ui.slices.FannedImageSlice
import app.deckbox.features.decks.public.ui.slices.HeaderSlice
import app.deckbox.features.decks.public.ui.slices.TagSlice

@Composable
fun DeckCardComposer(
  deck: Deck,
  config: DeckCardConfig,
  onEvent: (DeckCardEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val composeSlices = remember(deck, config) {
    config.slices.mapNotNull { slice ->
      when (slice) {
        Actions.Compact -> null // TODO
        Actions.Full -> ActionSlice(
          onTest = { onEvent(DeckCardEvent.Test) },
          onDuplicate = { onEvent(DeckCardEvent.Duplicate) },
          onDelete = { onEvent(DeckCardEvent.Delete) },
        )
        Description -> DescriptionSlice(deck.description ?: "")
        Header.Thumbnail -> null // TODO
        Header.Export -> HeaderSlice(deck) { onEvent(DeckCardEvent.Export) }
        Images.Fanned -> FannedImageSlice(deck)
        Images.Grid -> null // TODO
        Tags -> TagSlice(
          deck.tags.map { Tag(it, TagStyle.Outline) }
        )
      }
    }
  }

  DeckCard(
    slices = composeSlices,
    onClick = { onEvent(DeckCardEvent.Clicked) },
    modifier = modifier,
  )
}
