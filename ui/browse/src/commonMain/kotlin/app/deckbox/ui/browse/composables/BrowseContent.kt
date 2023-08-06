package app.deckbox.ui.browse.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.Pager
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import app.deckbox.common.compose.widgets.PlaceHolderPokemonCard
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.core.logging.bark
import app.deckbox.core.model.Card
import app.deckbox.ui.browse.BrowseUiState

@Composable
internal fun BrowseContent(
  state: BrowseUiState,
  onClick: (Card) -> Unit,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
) {
  BrowseContent(
    cardPager = state.cardsPager,
    onClick = onClick,
    modifier = modifier,
    contentPadding = contentPadding,
  )
}

@Composable
private fun Empty(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize()
  ) {

  }
}

@Composable
private fun Loading(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize()
  ) {
    SpinningPokeballLoadingIndicator(
      modifier = modifier.align(Alignment.Center)
    )
  }
}

@Composable
private fun BrowseContent(
  cardPager: Pager<Int, Card>,
  onClick: (Card) -> Unit,
  state: LazyGridState = rememberLazyGridState(),
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
) {
  val lazyPagingItems = cardPager.flow.collectAsLazyPagingItems()
  LazyVerticalGrid(
    columns = GridCells.Fixed(4),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = contentPadding,
    state = state,
    modifier = modifier.fillMaxSize(),
  ) {
    items(
      count = lazyPagingItems.itemCount,
      key = lazyPagingItems.itemKey { it.id },
    ) { index ->
      val item = lazyPagingItems[index]
      if (item != null) {
        PokemonCard(
          card = item,
          onClick = {
            bark { "${item.name} was clicked!" }
            onClick(item)
          },
        )
      } else {
        PlaceHolderPokemonCard()
      }
    }
  }

  if (lazyPagingItems.itemCount == 0 && lazyPagingItems.loadState.refresh is LoadStateLoading) {
    Loading()
  }
}
