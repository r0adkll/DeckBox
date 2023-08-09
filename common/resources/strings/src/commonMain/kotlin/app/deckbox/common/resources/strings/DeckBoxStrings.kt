package app.deckbox.common.resources.strings

import androidx.compose.ui.text.AnnotatedString

data class DeckBoxStrings(
  // Common / Misc
  val standardLegality: String,
  val expandedLegality: String,
  val unlimitedLegality: String,
  val genericEmptyCardsMessage: String,
  val genericSearchEmpty: (query: String?) -> AnnotatedString,

  // Decks
  val decks: String,
  val decksTabContentDescription: String,
  val deckDefaultNoName: String,
  val deckLastUpdated: (timestamp: String) -> String,
  val deckActionTestButton: String,
  val deckActionDuplicateButton: String,
  val deckActionDeleteButton: String,

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
