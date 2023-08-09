package app.deckbox.features.decks.public.ui.events

sealed interface DeckCardEvent {
  // Header
  object Export : DeckCardEvent

  // Actions
  object Delete : DeckCardEvent
  object Duplicate : DeckCardEvent
  object Test : DeckCardEvent

  // General
  object Clicked : DeckCardEvent
}
