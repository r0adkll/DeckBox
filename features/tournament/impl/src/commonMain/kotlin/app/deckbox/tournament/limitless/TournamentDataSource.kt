package app.deckbox.tournament.limitless

import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament

interface TournamentDataSource {

  suspend fun getTournaments(): List<Tournament>
  suspend fun getParticipants(tournament: Tournament): List<Participant>
  suspend fun getDeckList(tournament: Tournament, participant: Participant): DeckList
}
