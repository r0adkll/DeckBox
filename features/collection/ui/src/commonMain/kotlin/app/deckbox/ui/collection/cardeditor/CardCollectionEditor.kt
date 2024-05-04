package app.deckbox.ui.collection.cardeditor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.widgets.CollectionCounterListItem
import app.deckbox.common.compose.widgets.ContentLoadingSize
import app.deckbox.common.compose.widgets.DefaultEmptyView
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.CardCollectionEditorScreen
import app.deckbox.core.coroutines.LoadState
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.core.model.Card
import app.deckbox.core.model.CollectionCount
import app.deckbox.ui.collection.cardeditor.CardCollectionEditorUiEvent.Decrement
import app.deckbox.ui.collection.cardeditor.CardCollectionEditorUiEvent.Increment
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject

private val MinSheetHeight = 300.dp

@CircuitInject(MergeActivityScope::class, CardCollectionEditorScreen::class)
@Composable
fun CardCollectionEditor(
  state: CardCollectionEditorUiState,
  modifier: Modifier = Modifier,
) {
  val eventSink = state.eventSink

  Column(
    modifier = modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = MinSheetHeight)
      .padding(
        bottom = with(LocalDensity.current) {
          WindowInsets.navigationBars.getBottom(this).toDp()
        },
      ),
  ) {
    PokemonCard(
      url = state.cardImageUrl,
      name = state.cardName,
      collected = state.collectionCount.totalCount,
      onClick = {},
      modifier = Modifier
        .height(350.dp)
        .align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(16.dp))

    when (val variantsState = state.variants) {
      LoadState.Loading -> LoadingContent(modifier)
      LoadState.Error -> ErrorContent(modifier)
      is LoadState.Loaded -> LoadedContent(
        variants = variantsState.data,
        counts = state.collectionCount,
        onIncrementClick = { eventSink(Increment(it)) },
        onDecrementClick = { eventSink(Decrement(it)) },
      )
    }

    Spacer(Modifier.height(8.dp))
  }
}

@Composable
private fun LoadingContent(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(MinSheetHeight),
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
  DefaultEmptyView(
    modifier = modifier
      .fillMaxWidth()
      .height(MinSheetHeight),
  )
}

@Composable
fun LoadedContent(
  variants: List<Card.Variant>,
  counts: CollectionCount,
  onIncrementClick: (Card.Variant) -> Unit,
  onDecrementClick: (Card.Variant) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    variants.forEach { variant ->
      CollectionCounterListItem(
        title = when (variant) {
          Card.Variant.Normal -> LocalStrings.current.tcgPlayerNormal
          Card.Variant.Holofoil -> LocalStrings.current.tcgPlayerHolofoil
          Card.Variant.ReverseHolofoil -> LocalStrings.current.tcgPlayerReverseHolofoil
          Card.Variant.FirstEditionNormal -> LocalStrings.current.tcgPlayerFirstEditionNormal
          Card.Variant.FirstEditionHolofoil -> LocalStrings.current.tcgPlayerFirstEditionHolofoil
        },
        count = counts.forVariant(variant),
        onIncrementClick = { onIncrementClick(variant) },
        onDecrementClick = { onDecrementClick(variant) },
      )
    }
  }
}
