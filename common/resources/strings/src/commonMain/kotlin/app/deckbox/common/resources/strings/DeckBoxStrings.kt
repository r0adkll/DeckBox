package app.deckbox.common.resources.strings

data class DeckBoxStrings(
  // Common / Misc
  val standardLegality: String,
  val expandedLegality: String,
  val unlimitedLegality: String,

  // Decks
  val decks: String,
  val decksTabContentDescription: String,

  // Expansions
  val expansions: String,
  val expansionsTabContentDescription: String,
  val expansionReleaseDate: (date: String) -> String,
  val collection: String,
  val collectionCountOfTotal: (count: Int, total: Int) -> String,
  val expansionSearchHint: String,
  val expansionSearchEmptyMessage: (query: String) -> String,
  val expansionsEmptyMessage: String,
  val expansionsErrorMessage: String,

  // Browse
  val browse: String,
  val browseTabContentDescription: String,
  val browseSearchHint: String,
)
