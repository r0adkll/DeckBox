package app.deckbox.expansions.db

import app.deckbox.core.model.Expansion
import kotlinx.coroutines.flow.Flow

interface ExpansionsDao {

  suspend fun getExpansions(): List<Expansion>
  fun observeExpansions(): Flow<List<Expansion>>
  fun observeExpansion(id: String): Flow<Expansion>
  suspend fun insertExpansions(expansions: List<Expansion>)
  suspend fun deleteExpansion(id: String)
  suspend fun deleteExpansions()
}
