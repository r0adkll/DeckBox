package app.deckbox.features.boosterpacks.api

import app.deckbox.core.model.BoosterPack
import kotlinx.coroutines.flow.Flow

interface BoosterPackRepository {

  fun createSession(): String

  fun observeBoosterPacks(): Flow<List<BoosterPack>>
  fun observeBoosterPack(id: String): Flow<BoosterPack>

  fun editName(id: String, name: String)
  fun incrementCard(id: String, cardId: String, amount: Int = 1)
  fun decrementCard(id: String, cardId: String, amount: Int = 1)

  fun delete(id: String)
  fun duplicate(id: String)
}
