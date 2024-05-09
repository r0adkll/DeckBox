package app.deckbox.tournament.api.model

import app.deckbox.core.model.Format
import kotlinx.datetime.LocalDate

data class Tournament(
  val id: String,
  val name: String,
  val date: LocalDate,
  val country: String,
  val format: Format,
  val participantCount: Int,
  val winner: Winner,
) {

  data class Winner(
    val id: String,
    val name: String,
  )
}
