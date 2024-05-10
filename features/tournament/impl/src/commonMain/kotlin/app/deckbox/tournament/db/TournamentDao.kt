package app.deckbox.tournament.db

import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament

interface TournamentDao {

  suspend fun getTournaments(): List<Tournament>?
  suspend fun getParticipants(tournamentId: String): List<Participant>?
  suspend fun getDeckList(id: String): DeckList?

  suspend fun insertTournaments(tournaments: List<Tournament>)
  suspend fun insertParticipants(tournamentId: String, participants: List<Participant>)
  suspend fun insertDeckList(deckList: DeckList)
}
