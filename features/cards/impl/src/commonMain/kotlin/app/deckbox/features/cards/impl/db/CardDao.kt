package app.deckbox.features.cards.impl.db

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransactionCallbacks
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.features.cards.public.model.CardQuery
import app.deckbox.sqldelight.Cards
import kotlinx.coroutines.flow.Flow

interface CardDao {

  suspend fun fetch(id: String): Card?
  suspend fun fetch(ids: List<String>): List<Card>
  suspend fun fetch(query: CardQuery): List<Card>
  suspend fun fetchByExpansion(expansionId: String): List<Card>
  suspend fun fetchByRemoteKey(query: String, key: Int, onQuery: (Query<Cards>) -> Unit): List<Card>
  suspend fun fetchByRemoteKey(remoteKeyId: Long, onQuery: (Query<Cards>) -> Unit): List<Card>

  fun observe(id: String): Flow<Card>
  fun observe(ids: List<String>): Flow<List<Card>>
  fun observe(query: CardQuery): Flow<List<Card>>
  fun observeByExpansion(expansionId: String): Flow<List<Card>>
  fun observeByDeck(deckId: String): Flow<List<Stacked<Card>>>
  fun observeByBoosterPack(packId: String): Flow<List<Stacked<Card>>>
  fun observeByFavorites(): Flow<List<Card>>

  suspend fun insert(card: Card)
  suspend fun insert(cards: List<Card>)
  fun insert(callbacks: TransactionCallbacks, cards: List<Card>)

  suspend fun favorite(id: String, value: Boolean)
  fun observeFavorites(): Flow<Map<String, Boolean>>
  fun observeFavorite(id: String): Flow<Boolean>

  suspend fun delete(id: String)
  suspend fun delete(ids: List<String>)
  suspend fun deleteAll()
}
