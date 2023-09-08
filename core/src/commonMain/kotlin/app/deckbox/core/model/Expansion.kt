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

  companion object {
    const val FAVORITES = "favorites-id"

    val Favorites = Expansion(
      id = FAVORITES,
      name = "Favorites",
      releaseDate = LocalDate(2023, 1, 1),
      total = 0,
      series = "",
      printedTotal = 0,
      legalities = null,
      ptcgoCode = null,
      updatedAt = LocalDateTime(2023, 1, 1, 1, 0, 0, 0),
      images = Images("", ""),
    )
  }
}
