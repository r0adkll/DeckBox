package app.deckbox.tournament.api.model

import app.deckbox.core.model.Format
import kotlinx.datetime.LocalDate

data class Tournament(
  val name: String,
  val date: LocalDate,
  val country: String,
  val format: Format,
  val participantCount: Int,
  val winner: Winner,
) {

  data class Winner(
    val name: String,
    val country: String,
  )
}
