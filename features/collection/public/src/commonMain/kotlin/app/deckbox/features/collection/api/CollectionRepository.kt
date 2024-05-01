package app.deckbox.features.collection.api

import app.deckbox.core.model.CardId
import app.deckbox.core.model.Collection
import app.deckbox.core.model.CollectionCount
import app.deckbox.core.model.ExpansionId
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

  fun observeCollection(): Flow<Collection<ExpansionId>>
  fun observeCollectionForCard(cardId: String): Flow<CollectionCount>
  fun observeCollectionForExpansion(expansionId: ExpansionId): Flow<Collection<CardId>>

  suspend fun incrementCounts(
    cardId: String,
    normal: Int = 0,
    holofoil: Int = 0,
    reverseHolofoil: Int = 0,
    firstEditionNormal: Int = 0,
    firstEditionHolofoil: Int = 0,
  )
}
