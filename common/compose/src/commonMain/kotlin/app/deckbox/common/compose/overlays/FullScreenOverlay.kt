package app.deckbox.common.compose.overlays

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.Screen
import kotlinx.coroutines.launch

class FullScreenOverlay<Result : Any>(
  private val content: @Composable (OverlayNavigator<Result>) -> Unit,
) : Overlay<Result> {

  @Composable
  override fun Content(navigator: OverlayNavigator<Result>) {
    val coroutineScope = rememberCoroutineScope()

    // Delay setting the result until we've finished dismissing
    content { result ->
      // This is the OverlayNavigator.finish() callback
      coroutineScope.launch {
        navigator.finish(result)
      }
    }
  }
}

suspend fun OverlayHost.showInFullScreen(
  screen: Screen,
): Unit = show(
  FullScreenOverlay { navigator ->
    CircuitContent(
      screen = screen,
      onNavEvent = { event ->
        when (event) {
          NavEvent.Pop -> navigator.finish(Unit)
          else -> Unit
        }
      },
    )
  },
)
