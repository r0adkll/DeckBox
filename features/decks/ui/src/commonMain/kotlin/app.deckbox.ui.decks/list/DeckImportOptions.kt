package app.deckbox.ui.decks.list

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings

enum class DeckImportOptions(val text: @Composable () -> String) {
  Tournaments({ LocalStrings.current.importTournaments }),
  Text({ LocalStrings.current.importText }),
}
