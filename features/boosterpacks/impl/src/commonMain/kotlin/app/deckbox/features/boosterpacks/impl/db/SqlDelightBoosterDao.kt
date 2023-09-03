package app.deckbox.features.boosterpacks.impl.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.BoosterPack
import app.deckbox.core.time.FatherTime
import app.deckbox.db.mapping.asBoosterPacks
import app.deckbox.db.mapping.toModel
import app.deckbox.features.boosterpacks.impl.ids.BoosterPackIdGenerator
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ContributesBinding(MergeAppScope::class)
@Inject
class SqlDelightBoosterDao(
  private val database: DeckBoxDatabase,
  private val boosterPackIdGenerator: BoosterPackIdGenerator,
  private val fatherTime: FatherTime,
  private val dispatcherProvider: DispatcherProvider,
) : BoosterDao {

  override fun observe(id: String): Flow<BoosterPack> {
    return database.boosterPackQueries
      .getById(id)
      .asFlow()
      .mapToOne(dispatcherProvider.databaseRead)
      .map { it.toModel() }
  }

  override fun observeAll(): Flow<List<BoosterPack>> {
    return database.boosterPackQueries
      .getAll()
      .asFlow()
      .mapToList(dispatcherProvider.databaseRead)
      .map {
        it.map {
          it.toModel()
        }
      }
  }

  override suspend fun editName(id: String, name: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.boosterPackQueries.upsertName(
        id = id,
        name = name,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun incrementCard(id: String, cardId: String, amount: Int) {
    withContext(dispatcherProvider.databaseWrite) {
      database.boosterPackCardJoinQueries.incrementCount(
        boosterPackId = id,
        cardId = cardId,
        amount = amount,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun decrementCard(id: String, cardId: String, amount: Int) {
    withContext(dispatcherProvider.databaseWrite) {
      database.boosterPackCardJoinQueries.decrementCount(
        boosterPackId = id,
        cardId = cardId,
        amount = amount,
        updatedAt = fatherTime.now(),
        createdAt = fatherTime.now(),
      )
    }
  }

  override suspend fun delete(id: String) {
    withContext(dispatcherProvider.databaseWrite) {
      database.boosterPackQueries.delete(id)
    }
  }

  override suspend fun duplicate(id: String): String {
    return withContext(dispatcherProvider.databaseWrite) {
      database.transactionWithResult {
        val boosterPack = database.boosterPackQueries.getById(id).executeAsOne()
        val boosterPackCards = database.boosterPackCardJoinQueries.getByBoosterPack(id).executeAsList()

        val newBoosterPack = boosterPack.copy(
          id = boosterPackIdGenerator.generate(),
          updatedAt = fatherTime.now(),
          createdAt = fatherTime.now(),
        )
        val newCards = boosterPackCards.map { it.copy(boosterPackId = newBoosterPack.id) }

        database.boosterPackQueries.insert(newBoosterPack.asBoosterPacks())
        newCards.forEach { newCard ->
          database.boosterPackCardJoinQueries.insert(newCard)
        }

        newBoosterPack.id
      }
    }
  }
}
