package app.deckbox.common.resources.strings

import androidx.compose.ui.text.AnnotatedString

data class DeckBoxStrings(
  // Common / Misc
  val standardLegality: String,
  val expandedLegality: String,
  val unlimitedLegality: String,
  val genericEmptyCardsMessage: String,
  val genericSearchEmpty: (query: String?) -> AnnotatedString,
  val cardPlaceholderContentDescription: String,
  val refreshPricesContentDescription: String,

  // Decks
  val decks: String,
  val decksTabContentDescription: String,
  val deckDefaultNoName: String,
  val deckLastUpdated: (timestamp: String) -> String,
  val deckActionTestButton: String,
  val deckActionDuplicateButton: String,
  val deckActionDuplicateButtonContentDescription: String,
  val deckActionDeleteButton: String,
  val deckActionDeleteButtonContentDescription: String,
  val fabActionNewDeckButton: String,

  // Booster Packs
  val boosterPacks: String,
  val boosterPacksTitleLong: String,
  val boosterPacksTabContentDescription: String,

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

  // Card Detail
  val tcgPlayer: String,
  val tcgPlayerNormal: String,
  val tcgPlayerHolofoil: String,
  val tcgPlayerReverseHolofoil: String,
  val tcgPlayerFirstEditionHolofoil: String,
  val tcgPlayerFirstEditionNormal: String,

  val priceMarket: String,
  val priceLow: String,
  val priceMid: String,
  val priceHigh: String,

  val cardMarket: String,
  val priceTrend: String,
  val oneDayAvg: String,
  val sevenDayAvg: String,
  val thirtyDayAvg: String,

  val actionBuy: String,

  // Filter
  val lessThan: (Int) -> String,
  val lessThanEqual: (Int) -> String,
  val greaterThan: (Int) -> String,
  val greaterThanEqual: (Int) -> String,

  // Settings
  val settings: String,
  val settingsTabContentDescription: String,

  val decksEmptyStateMessage: String,
  val deckListHeaderPokemon: String,
  val deckListHeaderTrainer: String,
  val deckListHeaderEnergy: String,
  val cardCountInDeck: (count: Int) -> String,
  val deckTitleNoName: String,
)
