package app.deckbox.features.decks.api

import app.deckbox.core.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {

  fun observeDecks(): Flow<List<Deck>>

  fun deleteDeck(deck: Deck)
  fun duplicateDeck(deck: Deck)
}
