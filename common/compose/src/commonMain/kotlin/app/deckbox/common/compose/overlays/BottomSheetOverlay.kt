package app.deckbox.common.compose.overlays

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.PlatformBackHandler
import com.moriatsushi.insetsx.statusBars
import com.moriatsushi.insetsx.systemBars
import com.slack.circuit.overlay.Overlay
import com.slack.circuit.overlay.OverlayNavigator
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
