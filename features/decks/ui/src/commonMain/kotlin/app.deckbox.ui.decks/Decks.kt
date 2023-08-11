package app.deckbox.ui.decks

import DeckBoxRootAppBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.decks.public.ui.DeckCard
import cafe.adriel.lyricist.LocalStrings
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, DecksScreen::class)
@Composable
internal fun Decks(
  state: DecksUiState,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      DeckBoxRootAppBar(
        title = LocalStrings.current.decks,
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        text = { Text("Create") },
        icon = {
          Icon(Icons.Rounded.Add, contentDescription = null)
        },
        onClick = {
          // TODO: Navigate to Deck Builder Screen
        }
      )
    }
  ) { _ ->
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .padding(horizontal = 16.dp),
    ) {
      items(state.decks) { deck ->
        DeckCard(
          deck = deck,
          config = state.deckCardConfig,
          onEvent = {},
        )
      }
    }

    if (state.isLoading) {
      Box(Modifier.fillMaxSize()) {
        SpinningPokeballLoadingIndicator(size = 96.dp)
      }
    }
  }
}
