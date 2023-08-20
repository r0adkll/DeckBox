package app.deckbox.ui.filter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardFilter(
  state: FilterUiState,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(0.dp),
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = contentPadding,
  ) {
    state.specs.forEachIndexed { index, spec ->
      with(spec) {
        content(
          uiState = state,
          showDivider = index > 0,
          actionEmitter = state.eventSink,
        )
      }
    }
  }
}
