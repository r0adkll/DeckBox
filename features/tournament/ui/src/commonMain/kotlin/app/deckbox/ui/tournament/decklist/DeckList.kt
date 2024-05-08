package app.deckbox.ui.tournament.decklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.plus
import app.deckbox.common.compose.icons.rounded.Import
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.compose.widgets.builder.composables.CardList
import app.deckbox.common.compose.widgets.builder.model.CardUiModel
import app.deckbox.common.screens.DeckListScreen
import app.deckbox.core.CurrencyType
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import app.deckbox.ui.tournament.decklist.composables.PriceChip
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, DeckListScreen::class)
@Composable
fun DeckList(
  state: DeckListUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink

  val lazyGridState = rememberLazyGridState()
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    contentWindowInsets = WindowInsets.systemBars,
    topBar = {
      LargeTopAppBar(
        title = { Text(state.archetypeName) },
        navigationIcon = {
          IconButton(
            onClick = { eventSink(DeckListUiEvent.NavigateBack) },
          ) {
            Icon(
              Icons.AutoMirrored.Rounded.ArrowBack,
              contentDescription = null,
            )
          }
        },
        actions = {
          when (val deckListState = state.deckListState) {
            is LoadState.Loaded -> {
              // TODO: We should make the currency type a user preference that they can change and we can
              //  read here to determine what price to show them.
              val usdPrice = deckListState.data.price[CurrencyType.USD]
              if (usdPrice != null) {
                PriceChip(
                  price = usdPrice,
                  currencyType = CurrencyType.USD,
                  onClick = {
                    eventSink(DeckListUiEvent.PurchaseDeck(deckListState.data.bulkPurchaseUrl))
                  },
                  modifier = Modifier.padding(end = 16.dp),
                )
              }
            }

            else -> {
              // Do nothing
            }
          }
        },
        scrollBehavior = scrollBehavior,
      )
    },
    floatingActionButton = {
      val isExpanded by remember {
        derivedStateOf {
          lazyGridState.firstVisibleItemIndex == 0
        }
      }
      ExtendedFloatingActionButton(
        text = { Text(LocalStrings.current.fabActionImport) },
        icon = { Icon(Icons.Rounded.Import, contentDescription = null) },
        expanded = isExpanded,
        onClick = { eventSink(DeckListUiEvent.Import) },
      )
    },
  ) { paddingValues ->
    when (val cardsState = state.cardsLoadState) {
      is LoadState.Loaded -> LoadedContent(
        models = cardsState.data,
        onCardClick = { eventSink(DeckListUiEvent.CardClick(it.card)) },
        contentPadding = paddingValues,
        lazyGridState = lazyGridState,
        columns = 4,
        cardSpacing = 8.dp,
      )

      LoadState.Error -> ErrorContent(Modifier.padding(paddingValues))
      LoadState.Loading -> LoadingContent(Modifier.padding(paddingValues))
    }
  }
}

@Composable
private fun LoadingContent(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    SpinningPokeballLoadingIndicator(
      size = ContentLoadingSize,
    )
  }
}

@Composable
private fun ErrorContent(
  modifier: Modifier = Modifier,
) {
  DefaultEmptyView(modifier = modifier)
}

@Composable
private fun LoadedContent(
  models: ImmutableList<CardUiModel>,
  onCardClick: (Stacked<Card>) -> Unit,
  columns: Int,
  cardSpacing: Dp,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(),
  lazyGridState: LazyGridState = rememberLazyGridState(),
  headerContent: LazyGridScope.() -> Unit = {},
) {
  CardList(
    models = models,
    onCardClick = onCardClick,
    lazyGridState = lazyGridState,
    contentPadding = contentPadding + PaddingValues(bottom = 88.dp),
    modifier = modifier,
    columns = columns,
    cardSpacing = cardSpacing,
    headerContent = headerContent,
  )
}
