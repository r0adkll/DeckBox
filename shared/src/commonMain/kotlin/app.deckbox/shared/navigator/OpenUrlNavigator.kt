package app.deckbox.shared.navigator

import app.deckbox.common.screens.UrlScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList

class OpenUrlNavigator(
  private val navigator: Navigator,
  private val onOpenUrl: (String) -> Unit,
) : Navigator {
  override fun goTo(screen: Screen) {
    when (screen) {
      is UrlScreen -> onOpenUrl(screen.url)
      else -> navigator.goTo(screen)
    }
  }

  override fun pop(result: PopResult?): Screen? {
    return navigator.pop(result)
  }

  override fun resetRoot(newRoot: Screen, saveState: Boolean, restoreState: Boolean): ImmutableList<Screen> {
    return navigator.resetRoot(newRoot, saveState, restoreState)
  }

  override fun peek(): Screen? {
    return navigator.peek()
  }

  override fun peekBackStack(): ImmutableList<Screen> {
    return navigator.peekBackStack()
  }
}
