package app.deckbox.tournament.api.model

data class Participant(
  val place: Int,
  val name: String,
  val country: String,
  val archetype: DeckArchetype,
)
