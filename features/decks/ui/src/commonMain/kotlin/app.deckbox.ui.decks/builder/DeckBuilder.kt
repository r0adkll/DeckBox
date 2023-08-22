package app.deckbox.ui.decks.builder

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.deckbox.common.screens.DeckBuilderScreen
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@Suppress("UNUSED_PARAMETER")
@CircuitInject(MergeActivityScope::class, DeckBuilderScreen::class)
@Composable
fun DeckBuilder(
  state: DeckBuilderUiState,
  modifier: Modifier = Modifier,
) {
}
