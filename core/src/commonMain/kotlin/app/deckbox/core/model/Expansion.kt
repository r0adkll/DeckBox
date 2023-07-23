package app.deckbox.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class Expansion(
  val id: String,
  val name: String,
  val releaseDate: LocalDate,
  val total: Int,
  val series: String,
  val printedTotal: Int,
  val legalities: Legalities?,
  val ptcgoCode: String?,
  val updatedAt: LocalDateTime,
  val images: Images,
) {

  data class Images(
    val symbol: String,
    val logo: String,
  )
}
