package app.deckbox.features.collection.impl.db

import app.deckbox.core.model.CardId
import app.deckbox.core.model.Collection
import app.deckbox.core.model.CollectionCount
import app.deckbox.core.model.ExpansionId
import kotlinx.coroutines.flow.Flow

interface CollectionDao {

  fun observeByCard(cardId: String): Flow<CollectionCount>
  fun observeByExpansion(expansionId: String): Flow<Collection<CardId>>
  fun observeAll(): Flow<Collection<ExpansionId>>

  suspend fun incrementCounts(
    cardId: String,
    normal: Int = 0,
    holofoil: Int = 0,
    reverseHolofoil: Int = 0,
    firstEditionNormal: Int = 0,
    firstEditionHolofoil: Int = 0,
  )
}
