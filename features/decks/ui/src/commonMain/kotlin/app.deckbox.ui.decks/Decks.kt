package app.deckbox.ui.decks

import DeckBoxRootAppBar
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.navigation.DetailNavigation
import app.deckbox.common.compose.navigation.LocalDetailNavigation
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.DecksScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Deck
import app.deckbox.core.settings.DeckCardConfig
import app.deckbox.features.decks.public.ui.DeckCard
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
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
  var isTopBarElevated by remember { mutableStateOf(false) }

  Scaffold(
    modifier = modifier,
    topBar = {
      if (detailNavigationState is DetailNavigation.None) {
        DeckBoxRootAppBar(
          title = LocalStrings.current.decks,
          actions = {
            IconButton(
              onClick = { /*TODO: Nav to settings screen*/ },
            ) {
              Icon(Icons.Rounded.Settings, contentDescription = null)
            }
          },
          elevation = animateDpAsState(
            if (isTopBarElevated) 6.dp else 0.dp,
          ).value,
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
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
  ) { paddingValues ->
    CompactDeckContent(
      decks = state.decks,
      deckCardConfig = state.deckCardConfig,
      contentPadding = paddingValues,
      onScrolled = { isTopBarElevated = it },
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
  onScrolled: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  val state = rememberLazyListState()
  val isScrolled by remember {
    derivedStateOf {
      state.firstVisibleItemIndex > 0 ||
        state.firstVisibleItemScrollOffset > 0
    }
  }

  LaunchedEffect(isScrolled) {
    onScrolled(isScrolled)
  }

  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = contentPadding,
    modifier = modifier,
    state = state,
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
