package app.deckbox.ui.expansions.detail

import DeckBoxAppBar
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.navigation.isInDetailMode
import app.deckbox.common.compose.overlays.showInFullScreen
import app.deckbox.common.compose.widgets.PokeballLoadingIndicator
import app.deckbox.common.compose.widgets.PokemonCardGrid
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.di.MergeActivityScope
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Composable
internal fun ExpansionDetail(
  state: ExpansionDetailUiState,
  modifier: Modifier = Modifier,
) {
  val isDetailMode = isInDetailMode()

  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current

  val scrollState = rememberLazyGridState()
  val isScrolled by remember {
    derivedStateOf {
      scrollState.firstVisibleItemIndex > 0 ||
        (scrollState.firstVisibleItemIndex == 0 && scrollState.firstVisibleItemScrollOffset > 0)
    }
  }

  val elevation by animateDpAsState(
    if (isScrolled) 8.dp else 0.dp,
  )

  Scaffold(
    topBar = {
      if (state is ExpansionDetailUiState.Loaded) {
        DeckBoxAppBar(
          title = state.expansion.name,
          navigationIcon = {
            IconButton(
              onClick = {
                state.eventSink(ExpansionDetailUiEvent.NavigateBack)
              },
            ) {
              Icon(
                if (isDetailMode) Icons.Rounded.Close else Icons.Rounded.ArrowBack,
                contentDescription = null,
              )
            }
          },
          modifier = Modifier
            .graphicsLayer(shadowElevation = with(LocalDensity.current) { elevation.toPx() }),
        )
      }
    },
    modifier = modifier,
  ) { paddingValues ->
    when (state) {
      ExpansionDetailUiState.Loading -> Loading(Modifier.padding(paddingValues))
      is ExpansionDetailUiState.Loaded -> PokemonCardGrid(
        cardPager = state.cardsPager,
        state = scrollState,
        onClick = { card ->
          coroutineScope.launch {
            overlayHost.showInFullScreen(CardDetailScreen(card.id))
          }
        },
        contentPadding = paddingValues,
        modifier = Modifier.padding(horizontal = 16.dp),
      )
    }
  }
}

@Composable
private fun Loading(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    PokeballLoadingIndicator(
      size = 92.dp,
    )
  }
}
