package app.deckbox.expansions

import app.deckbox.core.model.Expansion
import kotlinx.coroutines.flow.Flow

interface ExpansionsRepository {

  suspend fun getExpansion(id: String): Expansion
  suspend fun getExpansions(): List<Expansion>
  fun observeExpansions(): Flow<List<Expansion>>
}
