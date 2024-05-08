package app.deckbox.network

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion

interface PokemonTcgApi {
  suspend fun getCard(id: String): Result<Card>
  suspend fun getCards(filters: Map<String, String>? = null): Result<PagedResponse<Card>>
  suspend fun getExpansion(id: String): Result<Expansion>
  suspend fun getExpansions(): Result<List<Expansion>>
  suspend fun getExpansions(ptcgCodes: Set<String>): Result<List<Expansion>>
  suspend fun getRarities(): Result<List<String>>
  suspend fun getSubtypes(): Result<List<String>>
}
