package app.deckbox.features.collection.impl

import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.CardId
import app.deckbox.core.model.Collection
import app.deckbox.core.model.CollectionCount
import app.deckbox.core.model.ExpansionId
import app.deckbox.features.collection.api.CollectionRepository
import app.deckbox.features.collection.impl.db.CollectionDao
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@AppScope
@Inject
@ContributesBinding(MergeAppScope::class)
class DatabaseCollectionRepository(
  private val db: CollectionDao,
) : CollectionRepository {

  override fun observeCollection(): Flow<Collection<ExpansionId>> {
    return db.observeAll()
  }

  override fun observeCollectionForCard(cardId: String): Flow<CollectionCount> {
    return db.observeByCard(cardId)
  }

  override fun observeCollectionForExpansion(expansionId: ExpansionId): Flow<Collection<CardId>> {
    return db.observeByExpansion(expansionId)
  }

  override suspend fun incrementCounts(
    cardId: String,
    normal: Int,
    holofoil: Int,
    reverseHolofoil: Int,
    firstEditionNormal: Int,
    firstEditionHolofoil: Int,
  ) {
    db.incrementCounts(
      cardId = cardId,
      normal = normal,
      holofoil = holofoil,
      reverseHolofoil = reverseHolofoil,
      firstEditionNormal = firstEditionNormal,
      firstEditionHolofoil = firstEditionHolofoil,
    )
  }
}
