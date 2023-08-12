package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AdaptiveState {
  Compact,
  Expanded,
}

@Composable
fun AdaptiveContent(
  compact: @Composable () -> Unit,
  expanded: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  stateResolver: (maxWidth: Dp) -> AdaptiveState = { maxWidth ->
    if (maxWidth > 400.dp) AdaptiveState.Expanded else AdaptiveState.Compact
  },
) {
  BoxWithConstraints(modifier = modifier) {
    when (stateResolver(maxWidth)) {
      AdaptiveState.Compact -> compact()
      AdaptiveState.Expanded -> expanded()
    }
  }
}
