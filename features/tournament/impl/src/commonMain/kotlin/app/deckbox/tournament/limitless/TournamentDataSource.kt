package app.deckbox.tournament.limitless

import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament
import io.ktor.http.HttpStatusCode

interface TournamentDataSource {

  suspend fun getTournaments(): Result<List<Tournament>>
  suspend fun getParticipants(tournamentId: String): Result<List<Participant>>
  suspend fun getDeckList(deckListId: String): Result<DeckList>

  sealed class TournamentException(
    message: String? = null,
    cause: Throwable? = null,
  ) : Exception(message, cause) {
    class NetworkError(
      val statusCode: HttpStatusCode,
      cause: Throwable? = null,
    ) : TournamentException(cause = cause)
    data object ParseError : TournamentException()
  }
}
