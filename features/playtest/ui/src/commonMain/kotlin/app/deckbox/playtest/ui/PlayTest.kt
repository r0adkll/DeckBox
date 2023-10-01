package app.deckbox.playtest.ui

import Psyduck
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.Snorlax
import app.deckbox.common.compose.widgets.DefaultIconSize
import app.deckbox.common.compose.widgets.EmptyView
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.PlayTestScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.playtest.ui.composables.Arena
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@Suppress("UNUSED_PARAMETER")
@CircuitInject(MergeActivityScope::class, PlayTestScreen::class)
@Composable
fun PlayTest(
  state: PlayTestUiState,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
  ) {
    when (state) {
      PlayTestUiState.Loading -> SpinningPokeballLoadingIndicator(Modifier.align(Alignment.Center))
      is PlayTestUiState.Error -> {
        EmptyView(
          label = { Text("Uh-oh! Unable to load deck: Valid=${state.validation.isValid}") },
          image = {
            Image(
              DeckBoxIcons.Psyduck,
              contentDescription = null,
              modifier = Modifier.size(DefaultIconSize),
            )
          }
        )
      }
      is PlayTestUiState.InGame -> {
        Arena(state.board)
      }
    }
  }
}
