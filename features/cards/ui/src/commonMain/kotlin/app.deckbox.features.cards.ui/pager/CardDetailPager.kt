package app.deckbox.features.cards.ui.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import app.deckbox.common.screens.CardDetailPagerScreen
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.di.MergeActivityScope
import com.moriatsushi.insetsx.navigationBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.Navigator
import kotlin.math.absoluteValue
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@CircuitInject(MergeActivityScope::class, CardDetailPagerScreen::class)
@Composable
fun CardDetailPager(
  state: CardDetailPagerUiState,
  modifier: Modifier = Modifier,
) {
  val detailPage = state.detailPage
//  val eventSink = state.eventSink

  val pagerState = key(detailPage.initialIndex) {
    rememberPagerState(
      initialPage = detailPage.initialIndex,
    ) { detailPage.size }
  }

  Box(modifier) {
    CardDetailPager(
      navigator = state.navigator,
      detailPage = detailPage,
      pagerState = pagerState,
    )

    CardPageIndicatorPager(
      pagerState = pagerState,
      detailPage = detailPage,
      modifier = Modifier.align(Alignment.BottomCenter),
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardDetailPager(
  navigator: Navigator,
  detailPage: CardDetailPage,
  pagerState: PagerState,
  modifier: Modifier = Modifier,
) {
  HorizontalPager(
    state = pagerState,
    key = { detailPage.screens[it].cardId },
    modifier = modifier,
  ) { page ->
    val screen = detailPage.screens[page]

    CompositionLocalProvider(
      LocalCardPagerBottomOffset provides (CardIndicatorSize + CardIndicatorPadding * 2),
      LocalCardPagerEnabled provides true,
    ) {
      CircuitContent(
        screen = screen,
        navigator = navigator,
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardPageIndicatorPager(
  pagerState: PagerState,
  detailPage: CardDetailPage,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()

  val indicatorPagerState = key(detailPage.initialIndex) {
    rememberPagerState(
      initialPage = detailPage.initialIndex,
    ) { detailPage.size }
  }

  val fling = PagerDefaults.flingBehavior(
    state = indicatorPagerState,
    pagerSnapDistance = PagerSnapDistance.atMost(10),
  )

  // Sync changes from the main card pager to the indicator pager
  LaunchedEffect(pagerState.currentPage) {
    if (pagerState.currentPage != indicatorPagerState.currentPage) {
      indicatorPagerState.animateScrollToPage(pagerState.currentPage)
    }
  }

  var size by remember { mutableStateOf(IntSize.Zero) }
  val horizontalPadding = if (size != IntSize.Zero) {
    with(LocalDensity.current) {
      (size.width.toDp() - CardIndicatorSize) / 2
    }
  } else 100.dp

  HorizontalPager(
    state = indicatorPagerState,
    pageSize = PageSize.Fixed(CardIndicatorSize),
    beyondBoundsPageCount = 10,
    flingBehavior = fling,
    pageSpacing = 4.dp,
    key = { detailPage.screens[it].cardId },
    modifier = modifier
      .fillMaxWidth()
      .onSizeChanged { size = it }
      .windowInsetsPadding(WindowInsets.navigationBars),
    contentPadding = PaddingValues(
      horizontal = horizontalPadding,
      vertical = CardIndicatorPadding,
    ),
  ) { page ->
    CardIndicator(
      card = detailPage.screens[page],
      onClick = {
        coroutineScope.launch {
          pagerState.animateScrollToPage(page)
        }
      },
      page = page,
      pagerState = pagerState,
      isCurrent = pagerState.currentPage == page,
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CardIndicator(
  card: CardDetailScreen,
  onClick: () -> Unit,
  isCurrent: Boolean,
  pagerState: PagerState,
  page: Int,
  modifier: Modifier = Modifier,
) {
  val pageOffset = (
    (pagerState.currentPage - page) +
      pagerState.currentPageOffsetFraction
    ).absoluteValue

  val cornerRadius = lerp(
    start = CardIndicatorSize / 2,
    stop = 8.dp,
    fraction = 1f - pageOffset.coerceIn(0f, 1f),
  )

  val borderColor = androidx.compose.ui.graphics.lerp(
    MaterialTheme.colorScheme.outlineVariant,
    MaterialTheme.colorScheme.primary,
    1f - pageOffset.coerceIn(0f, 1f),
  )

  val borderWidth = lerp(
    start = 2.dp,
    stop = 4.dp,
    fraction = 1f - pageOffset.coerceIn(0f, 1f),
  )

  val shape = RoundedCornerShape(cornerRadius)
  Box(
    modifier = modifier
      .size(CardIndicatorSize)
      .clip(shape)
      .clickable(enabled = !isCurrent, onClick = onClick)
      .border(
        width = borderWidth,
        color = borderColor,
        shape = shape,
      ),
    contentAlignment = Alignment.Center,
  ) {
    val imageAction by key(card.cardImageLarge) {
      rememberImageAction(card.cardImageLarge!!)
    }

    Image(
      painter = rememberImageActionPainter(imageAction),
      contentDescription = card.cardName,
      contentScale = ContentScale.Crop,
      alignment = Alignment.TopCenter,
      modifier = Modifier.size(CardIndicatorSize),
    )

    if (imageAction is ImageAction.Loading) {
      Box(
        modifier = Modifier
          .size(CardIndicatorSize)
          .background(MaterialTheme.colorScheme.surfaceDim),
        contentAlignment = Alignment.Center,
      ) {
        CircularProgressIndicator()
      }
    }
  }
}

private val CardIndicatorSize = 56.dp
private val CardIndicatorPadding = 16.dp

val LocalCardPagerBottomOffset = staticCompositionLocalOf { 0.dp }
val LocalCardPagerEnabled = staticCompositionLocalOf { false }
