package app.deckbox.expansions.db

import app.cash.sqldelight.async.coroutines.awaitAsList
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.deckbox.DeckBoxDatabase
import app.deckbox.core.coroutines.DispatcherProvider
import app.deckbox.core.di.AppScope
import app.deckbox.core.di.MergeAppScope
import app.deckbox.core.model.Expansion
import com.r0adkll.kotlininject.merge.annotations.ContributesBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
@ContributesBinding(MergeAppScope::class)
class SqlDelightExpansionsDao(
  private val database: DeckBoxDatabase,
  private val dispatcherProvider: DispatcherProvider,
) : ExpansionsDao {

  override suspend fun getExpansions(): List<Expansion> = withContext(dispatcherProvider.databaseRead) {
    database.expansionQueries
      .getAll()
      .awaitAsList()
      .toModels()
  }

  override fun observeExpansions(): Flow<List<Expansion>> {
    return database.expansionQueries
      .getAll()
      .asFlow()
      .mapToList(dispatcherProvider.databaseRead)
      .map { it.toModels() }
  }

  override fun observeExpansion(id: String): Flow<Expansion> {
    return database.expansionQueries
      .getById(id)
      .asFlow()
      .mapToOne(dispatcherProvider.databaseRead)
      .map { it.toModel() }
  }

  override suspend fun insertExpansions(expansions: List<Expansion>) = withContext(dispatcherProvider.databaseWrite) {
    database.transaction {
      expansions.forEach { expansion ->
        database.expansionQueries.insert(expansion.toEntity())
      }
    }
  }

  override suspend fun deleteExpansion(id: String) = withContext(dispatcherProvider.databaseWrite) {
    database.expansionQueries.deleteById(id)
  }

  override suspend fun deleteExpansions() = withContext(dispatcherProvider.databaseWrite) {
    database.expansionQueries.deleteAll()
  }
}
