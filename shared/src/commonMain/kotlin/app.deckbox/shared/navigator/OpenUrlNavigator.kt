package app.deckbox.shared.navigator

import app.deckbox.common.screens.UrlScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen

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

  override fun pop(): Screen? = navigator.pop()
  override fun resetRoot(newRoot: Screen): List<Screen> = navigator.resetRoot(newRoot)
}
