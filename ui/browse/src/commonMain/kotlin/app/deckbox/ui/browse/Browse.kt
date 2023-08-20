package app.deckbox.ui.browse

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.widgets.AdaptiveExpandedThreshold
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.FilterIcon
import app.deckbox.common.compose.widgets.PokemonCardGrid
import app.deckbox.common.compose.widgets.SearchBar
import app.deckbox.common.compose.widgets.SearchBarHeight
import app.deckbox.common.compose.widgets.SearchEmptyView
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.ui.filter.CardFilter
import app.deckbox.ui.filter.FilterState
import app.deckbox.ui.filter.FilterUiEvent
import app.deckbox.ui.filter.SearchBarWithFilter
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.statusBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

@CircuitInject(MergeActivityScope::class, BrowseScreen::class)
@Composable
internal fun Browse(
  state: BrowseUiState,
  modifier: Modifier = Modifier,
) {
  Surface(
    modifier = modifier,
  ) {
    BoxWithConstraints(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.TopStart,
    ) {
      var filterState by remember { mutableStateOf(FilterState.HIDDEN) }

      SearchBarWithFilter(
        filterState = filterState,
        onClose = { filterState = FilterState.HIDDEN },
        onClear = { state.filterUiState.eventSink(FilterUiEvent.ClearFilter) },
        searchBar = {
          SearchBar(
            initialValue = state.query,
            onQueryUpdated = { query ->
              state.eventSink(BrowseUiEvent.SearchUpdated(query))
            },
            onQueryCleared = {
              state.eventSink(BrowseUiEvent.SearchCleared)
            },
            leading = {
              Icon(
                Icons.Rounded.Search,
                contentDescription = null,
              )
            },
            placeholder = { Text(LocalStrings.current.browseSearchHint) },
            trailing = {
              IconButton(
                onClick = {
                  filterState = FilterState.VISIBLE
                },
              ) {
                FilterIcon(
                  isEmpty = state.filterUiState.filter.isEmpty,
                )
              }
            },
            modifier = Modifier
              .windowInsetsPadding(WindowInsets.statusBars)
              .padding(horizontal = 16.dp)
              .zIndex(1f),
          )
        },
        filterTitle = { Text("Filter") },
        filter = {
          CardFilter(
            state = state.filterUiState,
          )
        },
        modifier = Modifier.zIndex(1f),
      )

      val numColumns = if (maxWidth > AdaptiveExpandedThreshold) 6 else 4
      PokemonCardGrid(
        cardPager = state.cardsPager,
        onClick = { card ->
          state.eventSink(BrowseUiEvent.CardClicked(card))
        },
        contentPadding = PaddingValues(
          start = 16.dp,
          end = 16.dp,
          top = 8.dp + SearchBarHeight / 2,
        ),
        emptyContent = {
          if (!state.query.isNullOrBlank() || !state.filterUiState.filter.isEmpty) {
            SearchEmptyView(query = state.query)
          } else {
            DefaultEmptyView()
          }
        },
        columns = numColumns,
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.statusBars)
          .padding(top = 8.dp + SearchBarHeight / 2),
      )
    }
  }
}
