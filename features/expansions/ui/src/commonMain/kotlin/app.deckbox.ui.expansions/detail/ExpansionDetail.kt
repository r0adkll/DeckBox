package app.deckbox.ui.expansions.detail

import DeckBoxAppBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.FilterIcon
import app.deckbox.common.compose.widgets.PokeballLoadingIndicator
import app.deckbox.common.compose.widgets.PokemonCardGrid
import app.deckbox.common.screens.ExpansionDetailScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.settings.columnsForStyles
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.CardSelected
import app.deckbox.ui.expansions.detail.ExpansionDetailUiEvent.ChangeGridStyle
import app.deckbox.ui.expansions.detail.composables.ExpansionDetailFilter
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, ExpansionDetailScreen::class)
@Composable
internal fun ExpansionDetail(
  state: ExpansionDetailUiState,
  modifier: Modifier = Modifier,
) {
  var isFilterVisible by remember { mutableStateOf(false) }

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  Scaffold(
    topBar = {
      if (state is ExpansionDetailUiState.Loaded) {
        DeckBoxAppBar(
          title = state.expansion.name,
          navigationIcon = {
            IconButton(
              onClick = { state.eventSink(ExpansionDetailUiEvent.NavigateBack) },
            ) {
              Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
              )
            }
          },
          actions = {
            IconButton(
              onClick = { isFilterVisible = true },
            ) {
              FilterIcon(
                isEmpty = state.filterState.filter.isEmpty,
              )
            }
          },
          scrollBehavior = scrollBehavior,
        )
      }
    },
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
  ) { paddingValues ->
    when (state) {
      ExpansionDetailUiState.Loading -> Loading(Modifier.padding(paddingValues))
      is ExpansionDetailUiState.Loaded -> PokemonCardGrid(
        cards = state.cards,
        columns = state.cardGridStyle.columnsForStyles(),
        onClick = { card ->
          state.eventSink(CardSelected(card))
        },
        contentPadding = paddingValues,
        modifier = Modifier.padding(horizontal = 16.dp),
      )
    }
  }

  val filterLazyListState = rememberLazyListState()
  if (state is ExpansionDetailUiState.Loaded) {
    AnimatedVisibility(
      visible = isFilterVisible,
      enter = expandVertically(
        expandFrom = Alignment.Top,
      ) + fadeIn(),
      exit = shrinkVertically(
        shrinkTowards = Alignment.Top,
      ) + fadeOut(),
    ) {
      ExpansionDetailFilter(
        state = state.filterState,
        lazyListState = filterLazyListState,
        cardGridStyle = state.cardGridStyle,
        onClose = { isFilterVisible = false },
        onChangeGridStyle = { state.eventSink(ChangeGridStyle(it)) },
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
