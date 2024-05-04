package app.deckbox.common.compose.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.Pager
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.model.Card
import com.valentinilk.shimmer.shimmer
import kotlin.math.floor
import kotlin.math.roundToInt

private const val DefaultColumns = 4
private val DefaultVerticalItemSpacing = 8.dp
private val DefaultHorizontalItemSpacing = 8.dp

data class CardCounts(
  val count: Int,
  val collected: Int?,
)

@Suppress("USELESS_IS_CHECK")
@Composable
fun PokemonCardGrid(
  cardPager: Pager<Int, Card>,
  onClick: (Card) -> Unit,
  modifier: Modifier = Modifier,
  onLongClick: (Card) -> Unit = {},
  state: LazyGridState = rememberLazyGridState(),
  contentPadding: PaddingValues = PaddingValues(),
  columns: Int = DefaultColumns,
  countSelector: (cardId: String) -> CardCounts? = { null },
  emptyContent: @Composable () -> Unit = { DefaultEmptyView() },
) {
  val lazyPagingItems = cardPager.flow.collectAsLazyPagingItems()
  LazyVerticalGrid(
    columns = GridCells.Fixed(columns),
    verticalArrangement = Arrangement.spacedBy(DefaultVerticalItemSpacing),
    horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalItemSpacing),
    contentPadding = contentPadding,
    state = state,
    modifier = modifier.fillMaxSize(),
  ) {
    items(
      count = lazyPagingItems.itemCount,
      key = lazyPagingItems.itemKey { it.id },
    ) { index ->
      val item = lazyPagingItems[index]
      val cardCount = item?.let { countSelector(it.id) }
      PokemonCard(
        card = item,
        onClick = { item?.let(onClick) },
        onLongClick = { item?.let(onLongClick) },
        count = cardCount?.count,
        collected = cardCount?.collected,
      )
    }
  }

  val isPagerEmpty = lazyPagingItems.itemCount == 0
  val isRefreshing = lazyPagingItems.loadState.refresh is LoadStateLoading

  if (isPagerEmpty && isRefreshing) {
    ShimmerLoadingGrid(
      columns = columns,
      contentPadding = contentPadding,
      modifier = modifier.fillMaxSize(),
    )
  } else if (isPagerEmpty && !isRefreshing) {
    emptyContent()
  }
}

@Composable
fun PokemonCardGrid(
  cards: LoadState<out List<Card>>,
  onClick: (Card) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyGridState = rememberLazyGridState(),
  contentPadding: PaddingValues = PaddingValues(),
  columns: Int = DefaultColumns,
  headerContent: LazyGridScope.() -> Unit = {},
  emptyContent: @Composable () -> Unit = { DefaultEmptyView() },
  itemContent: @Composable (Card) -> Unit = { card ->
    PokemonCard(
      card = card,
      onClick = { onClick(card) },
    )
  },
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(columns),
    verticalArrangement = Arrangement.spacedBy(DefaultVerticalItemSpacing),
    horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalItemSpacing),
    contentPadding = contentPadding,
    state = state,
    modifier = modifier.fillMaxSize(),
  ) {
    headerContent()
    if (cards is LoadState.Loaded) {
      items(
        items = cards.data,
        key = { it.id },
      ) { card ->
        itemContent(card)
      }
    }
  }

  val isEmpty = (cards as? LoadState.Loaded)?.data?.isEmpty() == true
  val isRefreshing = cards is LoadState.Loading

  if (isRefreshing) {
    ShimmerLoadingGrid(
      columns = columns,
      contentPadding = contentPadding,
      modifier = modifier.fillMaxSize(),
    )
  } else if (isEmpty) {
    emptyContent()
  }
}

@Composable
fun ShimmerLoadingGrid(
  columns: Int,
  contentPadding: PaddingValues,
  modifier: Modifier = Modifier,
) = with(LocalDensity.current) {
  BoxWithConstraints(modifier = modifier) {
    // Compute size info
    val unPaddedWidth = maxWidth.toPx() - ((columns - 1) * DefaultHorizontalItemSpacing.toPx())
    val cardWidth = unPaddedWidth / columns
    val cardHeight = cardWidth / CardAspectRatio

    // Compute rows
    val rows = floor(maxHeight.toPx() / cardHeight).roundToInt()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)
        .shimmer(),
      verticalArrangement = Arrangement.spacedBy(DefaultVerticalItemSpacing),
    ) {
      repeat(rows) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalItemSpacing),
        ) {
          repeat(columns) {
            ShimmerPokemonCard(
              modifier = Modifier.weight(1f),
            )
          }
        }
      }
    }
  }
}
