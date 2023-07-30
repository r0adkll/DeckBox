package app.deckbox.common.resources.strings

import cafe.adriel.lyricist.LyricistStrings

@LyricistStrings(languageTag = "en", default = true)
val EnDeckBoxStrings = DeckBoxStrings(
  standardLegality = "Standard",
  expandedLegality = "Expanded",
  unlimitedLegality = "Unlimited",
  decks = "Decks",
  decksTabContentDescription = "List of saved decks",
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
)
