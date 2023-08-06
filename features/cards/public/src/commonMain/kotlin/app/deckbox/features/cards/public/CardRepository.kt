package app.deckbox.features.cards.public

import app.deckbox.core.model.Card
import kotlinx.coroutines.flow.Flow

interface CardRepository {

  suspend fun getCard(id: String): Card?
  suspend fun getCards(vararg id: String): List<Card>
  suspend fun getCards(ids: List<String>): List<Card>
  fun observeCards(ids: List<String>): Flow<List<Card>>
}