package app.deckbox.tournament.xml.builders

import app.deckbox.core.model.Format
import app.deckbox.tournament.api.model.Tournament
import kotlinx.datetime.LocalDate

class CompletedTournamentsBuilder {

  private val tournaments = mutableListOf<Tournament>()

  fun add(tournament: Tournament) {
    tournaments += tournament
  }

  fun build(): List<Tournament> {
    return tournaments
  }
}

class TournamentBuilder(
  private val name: String,
  private val date: LocalDate,
  private val country: String,
  private val format: Format,
  private val participantCount: Int,
  private val winnerName: String,
) {

  var id: String? = null
  var winnerId: String? = null

  fun build(): Tournament? {
    if (id != null && winnerId != null) {
      return Tournament(
        id = id!!,
        name = name,
        date = date,
        country = country,
        format = format,
        participantCount = participantCount,
        winner = Tournament.Winner(
          id = winnerId!!,
          name = winnerName,
        ),
      )
    } else {
      return null
    }
  }
}
