package app.deckbox.decks.impl.db

import app.deckbox.core.model.Card
import app.deckbox.core.model.Deck
import app.deckbox.core.model.Stacked
import kotlinx.coroutines.flow.Flow

interface DeckDao {

  fun observe(id: String): Flow<Deck>
  fun observeAll(): Flow<List<Deck>>

  suspend fun editName(deckId: String, name: String)
  suspend fun editDescription(deckId: String, description: String)
  suspend fun addTag(deckId: String, tag: String)
  suspend fun removeTag(deckId: String, tag: String)

  suspend fun incrementCard(deckId: String, cardId: String, amount: Int)
  suspend fun decrementCard(deckId: String, cardId: String, amount: Int)
  suspend fun removeCard(deckId: String, cardId: String)
  suspend fun addBoosterPack(deckId: String, boosterPackId: String)

  suspend fun delete(deckId: String)
  suspend fun duplicate(deckId: String): String

  suspend fun import(deckId: String, name: String, cards: List<Stacked<Card>>)
}
