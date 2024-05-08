package app.deckbox.tournament.api.model

data class DeckArchetype(
  val id: String,
  val name: String,
  val variant: String?,
  val symbols: List<String>,
)
