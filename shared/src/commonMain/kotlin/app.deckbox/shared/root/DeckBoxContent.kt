package app.deckbox.shared.root

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.deckbox.common.compose.LocalWindowSizeClass
import app.deckbox.common.compose.theme.DeckBoxTheme
import app.deckbox.ui.browse.BrowseUiFactory
import app.deckbox.ui.browse.BrowsePresenterFactory
import app.deckbox.ui.decks.DecksPresenterFactory
import app.deckbox.ui.decks.DecksUiFactory
import app.deckbox.ui.expansions.ExpansionsPresenterFactory
import app.deckbox.ui.expansions.ExpansionsUiFactory
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.Navigator

fun createCircuitConfig(): CircuitConfig {
  return CircuitConfig.Builder()

    // TODO: Move how these are provided to some sort of DI framework
    .addUiFactory(
      BrowseUiFactory(),
      DecksUiFactory(),
      ExpansionsUiFactory(),
    )
    .addPresenterFactory(
      BrowsePresenterFactory(),
      DecksPresenterFactory(),
      ExpansionsPresenterFactory(),
    )

    .build()
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DeckBoxContent(
  backstack: SaveableBackStack,
  navigator: Navigator,
  modifier: Modifier = Modifier,
) {
  val circuitConfig = remember { createCircuitConfig() }

  CompositionLocalProvider(
    LocalWindowSizeClass provides calculateWindowSizeClass(),
  ) {
    CircuitCompositionLocals(circuitConfig) {
      DeckBoxTheme {
        Home(
          backstack = backstack,
          navigator = navigator,
          modifier = modifier,
        )
      }
    }
  }
}
