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
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.Pager
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import app.deckbox.core.model.Card
import com.valentinilk.shimmer.shimmer
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val DefaultColumns = 4
private val DefaultVerticalItemSpacing = 8.dp
private val DefaultHorizontalItemSpacing = 8.dp

@Suppress("USELESS_IS_CHECK")
@Composable
fun PokemonCardGrid(
  cardPager: Pager<Int, Card>,
  onClick: (Card) -> Unit,
  modifier: Modifier = Modifier,
  state: LazyGridState = rememberLazyGridState(),
  contentPadding: PaddingValues = PaddingValues(),
  columns: Int = DefaultColumns,
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
      if (item != null) {
        PokemonCard(
          card = item,
          onClick = { onClick(item) },
        )
      } else {
        ShimmerPokemonCard()
      }
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
    val rows = ceil(maxHeight.toPx() / (cardHeight + DefaultVerticalItemSpacing.toPx())).roundToInt()

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
