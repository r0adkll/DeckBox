package app.deckbox.features.decks.api

import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Stacked
import kotlinx.coroutines.flow.Flow

interface DeckRepository {

  fun observeDecks(): Flow<List<Deck>>

  suspend fun deleteDeck(deckId: String)
  suspend fun duplicateDeck(deckId: String): String
  suspend fun importDeck(name: String, cards: List<Stacked<Card>>): String
}
