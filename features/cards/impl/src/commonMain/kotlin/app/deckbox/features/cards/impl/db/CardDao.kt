package app.deckbox.features.cards.impl.db

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.features.cards.public.model.CardQuery
import kotlinx.coroutines.flow.Flow

interface CardDao {

  suspend fun fetch(id: String): Card?
  suspend fun fetch(ids: List<String>): List<Card>
  suspend fun fetch(query: CardQuery): List<Card>
  suspend fun fetchByExpansion(expansionId: String): List<Card>

  fun observe(id: String): Flow<Card>
  fun observe(ids: List<String>): Flow<List<Card>>
  fun observe(query: CardQuery): Flow<List<Card>>
  fun observeByExpansion(expansionId: String): Flow<List<Card>>
  fun observeByDeck(deckId: String): Flow<List<Stacked<Card>>>
  fun observeByBoosterPack(packId: String): Flow<List<Stacked<Card>>>

  suspend fun insert(card: Card)
  suspend fun insert(cards: List<Card>)

  suspend fun delete(id: String)
  suspend fun delete(ids: List<String>)
  suspend fun deleteAll()
}
