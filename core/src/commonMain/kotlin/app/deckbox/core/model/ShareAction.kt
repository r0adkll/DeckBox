package app.deckbox.core.model

sealed interface ShareAction

data class DeckShareAction(
  val deck: Deck,
  val type: ExportType,
) : ShareAction {

  enum class ExportType {
    Text,
    Image,
  }
}
