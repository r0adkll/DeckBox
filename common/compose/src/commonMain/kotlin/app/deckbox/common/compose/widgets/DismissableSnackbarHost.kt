package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissableSnackbarHost(
  hostState: SnackbarHostState,
  modifier: Modifier = Modifier,
) {
  val dismissSnackbarState = rememberDismissState(
    confirmValueChange = { value ->
      if (value != DismissValue.Default) {
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
    SwipeToDismiss(
      state = dismissSnackbarState,
      background = {},
      dismissContent = { Snackbar(snackbarData = data) },
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth(),
    )
  }
}
