package app.deckbox.core.model

import kotlinx.datetime.LocalDateTime

data class Deck(
  val id: String,
  val name: String,
  val description: String?,
  val collectionMode: Boolean,
  val cardImages: List<String>,
  val legalities: Legalities,
  val tags: List<String>,
  val errors: List<ValidationError>,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)
