package app.deckbox.ui.decks

import DeckBoxRootAppBar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import app.deckbox.common.compose.navigation.DetailNavigation
import app.deckbox.common.compose.navigation.LocalDetailNavigation
import app.deckbox.common.compose.widgets.AdaptiveContent
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.public.ui.DeckCard
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, DecksScreen::class)
@Composable
internal fun Decks(
  state: DecksUiState,
  modifier: Modifier = Modifier,
) {
  val detailNavigationState = LocalDetailNavigation.current
  Scaffold(
    modifier = modifier,
    topBar = {
      if (detailNavigationState is DetailNavigation.None) {
        DeckBoxRootAppBar(
          title = LocalStrings.current.decks,
        )
      }
    },
    floatingActionButton = {
      if (detailNavigationState is DetailNavigation.None) {
        ExtendedFloatingActionButton(
          text = { Text("Create") },
          icon = {
            Icon(Icons.Rounded.Add, contentDescription = null)
          },
          onClick = {
            // TODO: Navigate to Deck Builder Screen
          },
        )
      }
    },
    contentWindowInsets = WindowInsets.systemBars,
  ) { paddingValues ->
    AdaptiveContent(
      compact = {
        CompactDeckContent(
          decks = state.decks,
          deckCardConfig = state.deckCardConfig,
          contentPadding = paddingValues,
        )
      },
      expanded = {
        ExpandedDeckContent(
          decks = state.decks,
          deckCardConfig = state.deckCardConfig,
          contentPadding = paddingValues,
        )
      },
      modifier = Modifier.padding(horizontal = 16.dp),
    )

    if (state.isLoading) {
      Box(Modifier.fillMaxSize()) {
        SpinningPokeballLoadingIndicator(size = 82.dp)
      }
    }
  }
}

@Composable
private fun CompactDeckContent(
  decks: List<Deck>,
  deckCardConfig: DeckCardConfig,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = contentPadding,
    modifier = modifier,
  ) {
    items(
      items = decks,
      key = { it.id },
    ) { deck ->
      DeckCard(
        deck = deck,
        config = deckCardConfig,
        onEvent = {},
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ExpandedDeckContent(
  decks: List<Deck>,
  deckCardConfig: DeckCardConfig,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
) {
  LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Fixed(2),
    verticalItemSpacing = 16.dp,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = contentPadding,
    modifier = modifier,
  ) {
    items(
      items = decks,
      key = { it.id },
    ) { deck ->
      DeckCard(
        deck = deck,
        config = deckCardConfig,
        onEvent = {},
      )
    }
  }
}
