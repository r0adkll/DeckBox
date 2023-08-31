package app.deckbox.core.model

import kotlinx.datetime.LocalDateTime

data class Deck(
  val id: String,
  val name: String,
  val description: String? = null,
  val tags: Set<String> = emptySet(),
  val cardImages: Set<String> = emptySet(),
  val legalities: Legalities = Legalities(null, null, null),
  val errors: List<ValidationError> = emptyList(),
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
)
