package app.deckbox.ui.expansions.list.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.deckbox.common.settings.DeckBoxSettings
import app.deckbox.core.settings.ExpansionCardStyle

@Composable
fun DeckBoxSettings.collectExpansionCardStyle(): State<ExpansionCardStyle> {
  return remember { observeExpansionCardStyle() }
    .collectAsState(ExpansionCardStyle.Large)
}
