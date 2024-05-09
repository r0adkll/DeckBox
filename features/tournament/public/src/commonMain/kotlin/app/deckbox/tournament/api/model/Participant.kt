package app.deckbox.tournament.api.model

data class Participant(
  val id: String,
  val name: String,
  val country: String,
  val place: Int,
  val archetype: DeckArchetype,
  val deckListId: String,
)
