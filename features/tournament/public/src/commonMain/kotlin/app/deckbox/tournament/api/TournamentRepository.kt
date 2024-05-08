package app.deckbox.tournament.api

import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament

interface TournamentRepository {

  suspend fun getTournaments(): Result<List<Tournament>>

  suspend fun getParticipants(tournamentId: String): Result<List<Participant>>

  suspend fun getDeckList(deckListId: String): Result<DeckList>
}
