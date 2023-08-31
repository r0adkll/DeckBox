package app.deckbox.features.decks.api

import app.deckbox.core.model.Deck
import kotlinx.coroutines.flow.Flow

interface DeckRepository {

  fun observeDecks(): Flow<List<Deck>>

  suspend fun deleteDeck(deckId: String)
  suspend fun duplicateDeck(deckId: String): String
}
