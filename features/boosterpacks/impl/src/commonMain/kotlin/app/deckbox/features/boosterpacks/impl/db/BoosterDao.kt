package app.deckbox.features.boosterpacks.impl.db

import app.deckbox.core.model.BoosterPack
import kotlinx.coroutines.flow.Flow

interface BoosterDao {

  fun observe(id: String): Flow<BoosterPack>
  fun observeAll(): Flow<List<BoosterPack>>

  suspend fun editName(id: String, name: String)
  suspend fun incrementCard(id: String, cardId: String, amount: Int)
  suspend fun decrementCard(id: String, cardId: String, amount: Int)

  suspend fun delete(id: String)
  suspend fun duplicate(id: String): String
}
