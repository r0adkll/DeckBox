package app.deckbox.tournament.api

import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament

interface TournamentRepository {

  suspend fun getTournaments(): List<Tournament>

  suspend fun getParticipants(tournament: Tournament): List<Participant>

  suspend fun getDeckList(tournament: Tournament, participant: Participant): DeckList
}
