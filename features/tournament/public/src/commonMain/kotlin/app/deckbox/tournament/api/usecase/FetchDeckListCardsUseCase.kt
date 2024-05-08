package app.deckbox.tournament.api.usecase

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.tournament.api.model.DeckList

interface FetchDeckListCardsUseCase {

  suspend fun execute(deckList: DeckList): Result<List<Stacked<Card>>>

  sealed class Errors(
    message: String? = null,
    cause: Throwable? = null,
  ) : Exception(message, cause) {
    data object ExpansionFetchError : Errors()
    data object CardsFetchError : Errors()
    data object CardsMappingError : Errors()
  }
}
