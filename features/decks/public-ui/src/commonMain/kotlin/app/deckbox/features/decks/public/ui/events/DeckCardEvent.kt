package app.deckbox.features.decks.public.ui.events

import app.deckbox.features.decks.public.ui.DeckExportOption

sealed interface DeckCardEvent {
  // Header
  data class Export(val option: DeckExportOption) : DeckCardEvent

  // Actions
  data object Delete : DeckCardEvent
  data object Duplicate : DeckCardEvent
  data object Test : DeckCardEvent

  // General
  data object Clicked : DeckCardEvent
}
