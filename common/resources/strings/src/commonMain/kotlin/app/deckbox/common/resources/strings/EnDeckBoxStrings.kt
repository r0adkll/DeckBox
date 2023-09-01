package app.deckbox.common.resources.strings

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "en", default = true)
val EnDeckBoxStrings = DeckBoxStrings(
  standardLegality = "Standard",
  expandedLegality = "Expanded",
  unlimitedLegality = "Unlimited",
  genericEmptyCardsMessage = "A Snorlax has blocked the path.\nPlease try another way.",
  genericSearchEmpty = { query ->
    buildAnnotatedString {
      append("Unable to find results for ")
      withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
        append("\"${query ?: "???"}\"")
      }
      append(".")
      append("\nYou've hurt yourself with confusion.")
    }
  },
  cardPlaceholderContentDescription = "Pokemon Card Placeholder",
  refreshPricesContentDescription = "Refresh Prices",

  decks = "Decks",
  decksTabContentDescription = "List of saved decks",
  deckDefaultNoName = "A deck has no name",
  deckLastUpdated = { timestamp -> "Last updated $timestamp" },
  deckActionTestButton = "Experiment",
  deckActionDuplicateButton = "Duplicate",
  deckActionDuplicateButtonContentDescription = "Duplicate deck",
  deckActionDeleteButton = "Delete",
  deckActionDeleteButtonContentDescription = "Delete deck",
  fabActionNewDeckButton = "New deck",

  expansions = "Expansions",
  expansionsTabContentDescription = "List of expansion sets",
  expansionReleaseDate = { "Released on $it" },
  collection = "Collection",
  collectionCountOfTotal = { count, total -> "$count of $total" },
  expansionSearchHint = "Search expansions",
  expansionSearchEmptyMessage = { "No expansions found for $it, please try searching again" },
  expansionsEmptyMessage = "No expansions found. Please check another castle.",
  expansionsErrorMessage = "Uh-oh! Looks like the expansion sets failed to load.",
  browse = "Browse",
  browseTabContentDescription = "Browse all Pokemon cards",
  browseSearchHint = "Search for any card",

  tcgPlayer = "TCGPlayer",
  tcgPlayerNormal = "Normal",
  tcgPlayerHolofoil = "Holo",
  tcgPlayerReverseHolofoil = "Reverse Holo",
  tcgPlayerFirstEditionHolofoil = "1st Edition Holo",
  tcgPlayerFirstEditionNormal = "1st Edition",
  priceMarket = "Market",
  priceLow = "Low",

  priceMid = "Mid",
  priceHigh = "High",
  cardMarket = "Cardmarket",
  priceTrend = "Price trend",

  oneDayAvg = "1 day avg",
  sevenDayAvg = "7 day avg",
  thirtyDayAvg = "30 day avg",
  actionBuy = "Buy",
  lessThan = { "Less than $it" },

  lessThanEqual = { "Less than or equal to $it" },

  greaterThan = { "Greater than $it" },
  greaterThanEqual = { "Greater than or equal to $it" },
  settings = "Settings",
  settingsTabContentDescription = "Change application settings",
  decksEmptyStateMessage = "Looks like you don't have any decks!\n" +
    "Try building one or importing an existing deck to see them appear here",
  deckListHeaderPokemon = "Pokémon",
  deckListHeaderTrainer = "Trainer",
  deckListHeaderEnergy = "Energy",
  cardCountInDeck = { count ->
    if (count == 1) {
      "$count Copy"
    } else {
      "$count Copies"
    }
  },
  deckTitleNoName = "Enter a name for your deck",
)
