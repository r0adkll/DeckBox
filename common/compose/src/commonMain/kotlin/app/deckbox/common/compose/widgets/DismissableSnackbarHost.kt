package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissableSnackbarHost(
  hostState: SnackbarHostState,
  modifier: Modifier = Modifier,
) {
  val dismissSnackbarState = rememberSwipeToDismissBoxState(
    confirmValueChange = { value ->
      if (value != SwipeToDismissBoxValue.Settled) {
        hostState.currentSnackbarData?.dismiss()
        true
      } else {
        false
      }
    },
  )

  SnackbarHost(
    hostState = hostState,
    modifier = modifier,
  ) { data ->
    SwipeToDismissBox(
      state = dismissSnackbarState,
      backgroundContent = {},
      content = { Snackbar(snackbarData = data) },
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    )
  }
}
