package app.deckbox.core.model

import kotlinx.datetime.LocalDateTime

data class Deck(
  val id: String,
  val name: String,
  val cards: Map<String, List<Card>>,
  val description: String?,
  val collectionMode: Boolean,
  val tags: List<String>,
  val errors: List<ValidationError>,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
) {

  val pokemonCards: List<Card> get() = cards[SuperType.POKEMON.name] ?: emptyList()
  val trainerCards: List<Card> get() = cards[SuperType.TRAINER.name] ?: emptyList()
  val energyCards: List<Card> get() = cards[SuperType.ENERGY.name] ?: emptyList()
}
