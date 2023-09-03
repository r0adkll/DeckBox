package app.deckbox.core.model

import kotlinx.datetime.LocalDateTime

data class BoosterPack(
  val id: String,
  val name: String? = null,
  val legalities: Legalities? = null,
  val cardImages: List<String> = emptyList(),
  val updatedAt: LocalDateTime,
  val createdAt: LocalDateTime,
)
