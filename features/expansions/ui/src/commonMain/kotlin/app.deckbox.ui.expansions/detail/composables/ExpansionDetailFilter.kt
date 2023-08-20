package app.deckbox.ui.expansions.detail.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterAltOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.SearchBarElevation
import app.deckbox.ui.filter.CardFilter
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.FilterUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExpansionDetailFilter(
  state: FilterUiState,
  onClose: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Filter") },
        navigationIcon = {
          IconButton(
            onClick = onClose,
          ) {
            Icon(Icons.Rounded.Close, contentDescription = null)
          }
        },
        actions = {
          IconButton(
            onClick = {
              state.eventSink(FilterUiEvent.ClearFilter)
            },
          ) {
            Icon(Icons.Rounded.FilterAltOff, contentDescription = null)
          }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(SearchBarElevation),
        ),
      )
    },
    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
    modifier = modifier,
  ) { paddingValues ->
    CardFilter(
      state = state,
      contentPadding = paddingValues,
    )
  }
}
