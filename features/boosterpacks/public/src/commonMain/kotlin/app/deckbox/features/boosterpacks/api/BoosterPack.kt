package app.deckbox.features.boosterpacks.api

import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import kotlinx.datetime.LocalDateTime

data class BoosterPack(
  val id: String,
  val name: String,
  val cards: List<Stacked<Card>>,
  val updatedAt: LocalDateTime,
  val createdAt: LocalDateTime,
)
