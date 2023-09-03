package app.deckbox.common.compose.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.PlatformBackHandler
import app.deckbox.common.screens.DeckBoxScreen
import app.deckbox.common.screens.OverlayResultScreen
import app.deckbox.core.logging.bark
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.overlay.OverlayNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.launch

class BottomSheetOverlay<Model : Any, Result : Any>(
  private val model: Model,
  private val onDismiss: (() -> Result)? = null,
  private val content: @Composable (Model, OverlayNavigator<Result>) -> Unit,
) : Overlay<Result> {

  @OptIn(ExperimentalMaterialApi::class)
  @Composable
  override fun Content(navigator: OverlayNavigator<Result>) {
    var hasShown by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
      initialValue = ModalBottomSheetValue.Hidden,
    )

    var pendingResult by remember { mutableStateOf<Result?>(null) }

    ModalBottomSheetLayout(
      sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
      sheetState = sheetState,
      sheetBackgroundColor = MaterialTheme.colorScheme.surface,
      sheetContent = {
        PlatformBackHandler(enabled = sheetState.isVisible) {
          coroutineScope
            .launch { sheetState.hide() }
            .invokeOnCompletion {
              if (!sheetState.isVisible) {
                navigator.finish(onDismiss!!.invoke())
              }
            }
        }

        Handle(
          Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = 22.dp),
        )

        content(model) { result ->
          // This is the OverlayNavigator.finish() callback
          coroutineScope.launch {
            pendingResult = result
            sheetState.hide()
          }
        }
      },
    ) {
      // No Content
    }

    LaunchedEffect(model, onDismiss) {
      snapshotFlow { sheetState.currentValue }
        .collect { newValue ->
          if (hasShown && newValue == ModalBottomSheetValue.Hidden) {
            // This is apparently as close as we can get to an "onDismiss" callback, which
            // unfortunately has no animation
            val result = pendingResult ?: onDismiss?.invoke() ?: error("no result!")
            navigator.finish(result)
          }
        }
    }
    LaunchedEffect(model, onDismiss) {
      // TODO why doesn't this ever hit if it's after show()
      hasShown = true
      sheetState.show()
    }
  }
}

@Composable
private fun Handle(
  modifier: Modifier = Modifier,
) {
  Spacer(
    modifier = modifier
      .size(32.dp, 4.dp)
      .background(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(50),
      ),
  )
}

suspend fun <R> OverlayHost.showBottomSheetScreen(screen: DeckBoxScreen): R? {
  bark { "Showing BottomSheetOverlay for $screen" }
  return show(
    BottomSheetOverlay<DeckBoxScreen, OverlayResultScreen<R>>(
      model = screen,
      onDismiss = { OverlayResultScreen() },
      content = { model, navigator ->
        val overlayNavigator = object : Navigator {

          @Suppress("UNCHECKED_CAST")
          override fun goTo(screen: Screen) {
            if (screen is OverlayResultScreen<*>) {
              bark { "Result: ${(screen as? OverlayResultScreen<R>)?.result}" }
              navigator.finish(
                screen as? OverlayResultScreen<R>
                  ?: error("Incorrect result type for the current overlay"),
              )
            } else {
              error("Navigation not supported in overlays")
            }
          }

          override fun pop(): Screen? {
            navigator.finish(OverlayResultScreen())
            return null
          }

          override fun resetRoot(newRoot: Screen): List<Screen> {
            error("Operation not allowed in overlays")
          }
        }

        CircuitContent(
          screen = model,
          navigator = overlayNavigator,
        )
      },
    ),
  ).result
}

fun <T> Navigator.overlayResult(result: T?) {
  goTo(OverlayResultScreen(result))
}
