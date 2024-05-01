package app.deckbox.features.collection.impl.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.CardId
import app.deckbox.core.model.Collection
import app.deckbox.core.model.CollectionCount
import app.deckbox.core.model.ExpansionId
import app.deckbox.core.time.FatherTime
import app.deckbox.db.mapping.toEntity
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@ContributesBinding(MergeAppScope::class)
class SqlDelightCollectionDao(
  private val database: DeckBoxDatabase,
  private val fatherTime: FatherTime,
  private val dispatcherProvider: DispatcherProvider,
) : CollectionDao {

  override fun observeByCard(cardId: String): Flow<CollectionCount> {
    return database.collectionQueries
      .getByCard(cardId)
      .asFlow()
      .mapToOneOrNull(dispatcherProvider.databaseRead)
      .map { it?.toEntity() ?: CollectionCount(cardId) }
  }

  override fun observeByExpansion(expansionId: String): Flow<Collection<CardId>> {
    return database.collectionQueries
      .sumsByExpansion(expansionId)
      .asFlow()
      .mapToList(dispatcherProvider.databaseRead)
      .map { expansionSums ->
        val counts = expansionSums.associate { it.cardId to it.expansionTotalCount.toInt() }
        Collection(counts)
      }
  }

  override fun observeAll(): Flow<Collection<ExpansionId>> {
    return database.collectionQueries
      .sumsByAll()
      .asFlow()
      .mapToList(dispatcherProvider.databaseRead)
      .map { sums ->
        val counts = sums.associate { it.expansionId to (it.expansionTotalCount.toInt()) }
        Collection(counts)
      }
  }

  override suspend fun incrementCounts(
    cardId: String,
    normal: Int,
    holofoil: Int,
    reverseHolofoil: Int,
    firstEditionNormal: Int,
    firstEditionHolofoil: Int,
  ) {
    withContext(dispatcherProvider.databaseWrite) {
      database.transaction {
        database.collectionQueries.incrementCounts(
          updatedAt = fatherTime.now(),
          cardId = cardId,
          normalAmount = normal,
          holofoilAmount = holofoil,
          reverseHolofoilAmount = reverseHolofoil,
          firstEditionNormalAmount = firstEditionNormal,
          firstEditionHolofoilAmount = firstEditionHolofoil,
        )
      }
    }
  }
}
