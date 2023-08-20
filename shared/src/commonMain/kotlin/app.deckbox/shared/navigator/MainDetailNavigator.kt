package app.deckbox.shared.navigator

import app.deckbox.common.screens.DeckBoxScreen
import app.deckbox.common.screens.RootScreen
import app.deckbox.common.screens.UrlScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import io.ktor.http.Url

/**
 * A custom navigator for automatically navigating certain marked [DeckBoxScreen]s
 * to the detail panel if side-detail navigation is enabled
 */
class MainDetailNavigator(
  private val mainNavigator: Navigator,
  private val detailNavigator: Navigator,
  private val isDetailEnabled: Boolean,
) : Navigator {
  override fun goTo(screen: Screen) {
    when (screen) {
      is DeckBoxScreen -> {
        if (isDetailEnabled && screen.presentation.isDetailScreen) {
          detailNavigator.resetRoot(screen)
        } else {
          mainNavigator.goTo(screen)
        }
      }

      else -> mainNavigator.goTo(screen)
    }
  }

  override fun pop(): Screen? {
    return mainNavigator.pop()
  }

  override fun resetRoot(newRoot: Screen): List<Screen> {
    // We should reset to root for the detail navigator too so that any side content
    // gets reset when the main nav context changes
    detailNavigator.resetRoot(RootScreen())
    return mainNavigator.resetRoot(newRoot)
  }
}
