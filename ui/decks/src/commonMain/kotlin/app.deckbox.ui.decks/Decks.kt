package app.deckbox.ui.decks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.deckbox.common.screens.DecksScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

class DecksUiFactory : Ui.Factory {
  override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
    is DecksScreen -> {
      ui<DecksUiState> { state, modifier ->
        Decks(state, modifier)
      }
    }

    else -> null
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Decks(
  state: DecksUiState,
  modifier: Modifier = Modifier,
) {
  print(state)
  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text("Decks") }, // TODO: Convert to use Lyracist,
        navigationIcon = {
          IconButton(
            onClick = {
              // TODO: Navigate back
            },
          ) {
            Icon(
              Icons.Default.ArrowBack,
              contentDescription = null,
            )
          }
        },
      )
    },
  ) { paddingValues ->
    // TODO: Replace with actual UI
    Box(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      Text("Decks UI using Circuit")
    }
  }
}