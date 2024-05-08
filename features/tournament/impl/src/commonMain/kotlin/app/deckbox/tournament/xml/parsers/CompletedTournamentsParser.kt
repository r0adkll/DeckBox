package app.deckbox.tournament.xml.parsers

import app.deckbox.core.model.Format
import app.deckbox.tournament.xml.SoupParser
import app.deckbox.tournament.xml.builders.CompletedTournamentsBuilder
import app.deckbox.tournament.xml.builders.TournamentBuilder
import kotlinx.datetime.LocalDate

class CompletedTournamentsParser(
  private val completedTournamentsBuilder: CompletedTournamentsBuilder,
) : SoupParser() {

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    if (name == "table" && attributes["class"]?.contains("completed-tournaments") == true) {
      // Push the tournament parser that is responsible for reading the actual tournament information
      push(TournamentParser(completedTournamentsBuilder))
    }
  }
}

class TournamentParser(
  private val completedTournamentsBuilder: CompletedTournamentsBuilder,
) : SoupParser() {

  private var currentTournamentBuilder: TournamentBuilder? = null

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    val tournamentName = attributes["data-name"]
    if (name == "tr" && tournamentName != null) {
      val format = attributes["data-format"]?.let { Format.of(it) } ?: return
      val countryId = attributes["data-country"] ?: return
      val date = attributes["data-date"]?.let { LocalDate.parse(it) } ?: return
      val playerCount = attributes["data-players"]?.toIntOrNull() ?: return
      val winnerName = attributes["data-winner"]?.substringAfter("-") ?: return

      currentTournamentBuilder = TournamentBuilder(
        name = tournamentName,
        date = date,
        country = countryId,
        format = format,
        participantCount = playerCount,
        winnerName = winnerName,
      )
    }
  }

  override fun onAttribute(name: String, value: String, quote: String?) {
    when (name) {
      "href" -> {
        if (value.startsWith("/tournaments/")) {
          val tournamentId = value.substringAfterLast("/")
          currentTournamentBuilder?.id = tournamentId
        } else if (value.startsWith("/players/")) {
          val winnerId = value.substringAfterLast("/")
          currentTournamentBuilder?.winnerId = winnerId
        }
      }
    }
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "tr" -> {
        val tournament = currentTournamentBuilder?.build() ?: return
        completedTournamentsBuilder.add(tournament)
      }
      "table" -> {
        pop()
      }
    }
  }
}
