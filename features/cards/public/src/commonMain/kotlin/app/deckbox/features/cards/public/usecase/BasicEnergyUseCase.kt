package app.deckbox.features.cards.public.usecase

import app.deckbox.core.model.Card
import app.deckbox.core.model.ExpansionId

/**
 * A re-usable use case that fetches one of each basic energy from a given expansion id.
 */
interface BasicEnergyUseCase {

  suspend fun execute(expansionId: ExpansionId = DefaultEnergyExpansionId): Result<List<Card>>

  sealed class Errors(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data object NoEnergyCardsFound : Errors("Unable to find any energy cards for that expansion")
    data class Unknown(override val cause: Throwable) : Errors("Unknown error occurred fetching energy", cause)
  }

  companion object {
    /**
     * The default energy set in the api is "sve" or Scarlet & Violet Energies
     */
    const val DefaultEnergyExpansionId = "sve"
  }
}
