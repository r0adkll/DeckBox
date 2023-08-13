package app.deckbox.ui.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.overlays.showInFullScreen
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.PokemonCardGrid
import app.deckbox.common.compose.widgets.SearchBar
import app.deckbox.common.compose.widgets.SearchBarHeight
import app.deckbox.common.compose.widgets.SearchEmptyView
import app.deckbox.common.screens.BrowseScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.logging.bark
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.statusBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch

@CircuitInject(MergeActivityScope::class, BrowseScreen::class)
@Composable
internal fun Browse(
  state: BrowseUiState,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()
  val overlayHost = LocalOverlayHost.current

  Surface(
    modifier = modifier,
  ) {
    BoxWithConstraints(
      modifier = Modifier
        .fillMaxSize(),
      contentAlignment = Alignment.TopStart,
    ) {
      SearchBar(
        initialValue = state.query,
        onQueryUpdated = { query ->
          bark { "Query: $query" }
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
              // TODO:
            },
          ) {
            Icon(Icons.Rounded.FilterAlt, contentDescription = "Filter your search")
          }
        },
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.statusBars)
          .zIndex(1f)
          .padding(horizontal = 16.dp),
      )

      val numColumns = if (maxWidth > 400.dp) 6 else 4
      PokemonCardGrid(
        cardPager = state.cardsPager,
        onClick = { card ->
          coroutineScope.launch {
            overlayHost.showInFullScreen(CardDetailScreen(card.id))
          }
        },
        contentPadding = PaddingValues(
          start = 16.dp,
          end = 16.dp,
          top = 16.dp + SearchBarHeight / 2,
        ),
        emptyContent = {
          if (!state.query.isNullOrBlank() || state.filter?.isEmpty == false) {
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
