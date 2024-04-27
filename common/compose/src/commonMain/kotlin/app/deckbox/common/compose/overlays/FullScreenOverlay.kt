package app.deckbox.common.compose.overlays

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList

class FullScreenOverlay<S : Screen>(private val screen: S) : Overlay<Unit> {
  @Composable
  override fun Content(navigator: OverlayNavigator<Unit>) {
//    val systemUiController = rememberSystemUiController()
//    val conditionalSystemUiColors = rememberConditionalSystemUiColors(systemUiController)
    val dispatchingNavigator = remember {
      DispatchingOverlayNavigator(navigator)
    }
    // TODO why doesn't this work?? Back still quits the activity
//    PlatformBackHandler(enabled = true, onBack = dispatchingNavigator::pop)
    CircuitContent(screen = screen, onNavEvent = { dispatchingNavigator.pop() })
  }
}

private class DispatchingOverlayNavigator(
  private val navigator: OverlayNavigator<Unit>,
//  private val conditionalSystemUiColors: ConditionalSystemUiColors,
) : Navigator {
  override fun goTo(screen: Screen) {
    error("goTo() is not supported in full screen overlays!")
  }

  override fun pop(result: PopResult?): Screen? {
    navigator.finish(Unit)
//    conditionalSystemUiColors.restore()
    return null
  }

  override fun resetRoot(newRoot: Screen, saveState: Boolean, restoreState: Boolean): ImmutableList<Screen> {
    error("resetRoot() is not supported in full screen overlays!")
  }

  override fun peek(): Screen? {
    TODO("Not yet implemented")
  }

  override fun peekBackStack(): ImmutableList<Screen> {
    TODO("Not yet implemented")
  }
}

suspend fun OverlayHost.showInFullScreen(
  screen: Screen,
): Unit = show(FullScreenOverlay(screen))
