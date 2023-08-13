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
  genericEmptyCardsMessage = "A Snorlax has blocked your way. Please try another path.",
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

  decks = "Decks",
  decksTabContentDescription = "List of saved decks",
  deckDefaultNoName = "A deck has no name",
  deckLastUpdated = { timestamp -> "Last updated $timestamp" },
  deckActionTestButton = "Test",
  deckActionDuplicateButton = "Duplicate",
  deckActionDeleteButton = "Delete",

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
  settings = "Settings",
  settingsTabContentDescription = "Change application settings",
)
