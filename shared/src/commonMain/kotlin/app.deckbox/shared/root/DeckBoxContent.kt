package app.deckbox.shared.root

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.deckbox.common.compose.LocalWindowSizeClass
import app.deckbox.common.compose.extensions.shouldUseDarkColors
import app.deckbox.common.compose.extensions.shouldUseDynamicColors
import app.deckbox.common.compose.theme.DeckBoxTheme
import app.deckbox.common.settings.DeckBoxSettings
import cafe.adriel.lyricist.ProvideStrings
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.runtime.Navigator
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias DeckBoxContent = @Composable (
  backstack: SaveableBackStack,
  navigator: Navigator,
  modifier: Modifier,
) -> Unit

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Inject
@Composable
fun DeckBoxContent(
  @Assisted backstack: SaveableBackStack,
  @Assisted navigator: Navigator,
  circuitConfig: CircuitConfig,
  imageLoader: Lazy<ImageLoader>,
  settings: DeckBoxSettings,
  @Assisted modifier: Modifier = Modifier,
) {
  CompositionLocalProvider(
    LocalWindowSizeClass provides calculateWindowSizeClass(),
    LocalImageLoader provides remember { imageLoader.value },
  ) {
    ProvideStrings {
      CircuitCompositionLocals(circuitConfig) {
        DeckBoxTheme(
          useDarkColors = settings.shouldUseDarkColors(),
          useDynamicColors = settings.shouldUseDynamicColors(),
        ) {
          Home(
            backstack = backstack,
            navigator = navigator,
            modifier = modifier,
          )
        }
      }
    }
  }
}
