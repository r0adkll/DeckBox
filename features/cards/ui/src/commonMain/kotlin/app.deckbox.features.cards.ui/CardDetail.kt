package app.deckbox.features.cards.ui

import DeckBoxAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.extensions.applyHoloAndDragEffect
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.icons.rounded.RemoveCard
import app.deckbox.common.compose.icons.rounded.SubtractCard
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.cards.ui.composables.CardMarketPriceCard
import app.deckbox.features.cards.ui.composables.InfoCard
import app.deckbox.features.cards.ui.composables.TcgPlayerPriceCard
import cafe.adriel.lyricist.LocalStrings
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, CardDetailScreen::class)
@Composable
internal fun CardDetail(
  state: CardDetailUiState,
  modifier: Modifier = Modifier,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  Scaffold(
    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      DeckBoxAppBar(
        title = state.deckState?.let {
          LocalStrings.current.cardCountInDeck(it.count)
        } ?: "",
        navigationIcon = {
          IconButton(
            onClick = { state.eventSink(CardDetailUiEvent.NavigateBack) },
          ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
          }
        },
        actions = {
          state.deckState?.let { deckState ->
            IconButton(
              enabled = deckState.count > 0,
              onClick = { state.eventSink(CardDetailUiEvent.DecrementCount) },
            ) {
              Icon(
                Icons.Rounded.SubtractCard,
                contentDescription = null,
              )
            }
            IconButton(
              onClick = { state.eventSink(CardDetailUiEvent.IncrementCount) },
            ) {
              Icon(
                Icons.Rounded.AddCard,
                contentDescription = null,
              )
            }
          }
        },
        scrollBehavior = scrollBehavior,
      )
    },
    floatingActionButton = {
      var isFavorited by remember { mutableStateOf(false) }
      FloatingActionButton(
        onClick = {
          isFavorited = !isFavorited
        },
      ) {
        Icon(
          if (isFavorited) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
          contentDescription = null,
        )
      }
    },
    contentWindowInsets = WindowInsets.systemBars.exclude(WindowInsets.navigationBars),
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .verticalScroll(rememberScrollState()),
    ) {
      CardImage(
        url = state.cardImageUrl,
        contentDescription = state.cardName,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            horizontal = 32.dp,
          )
          .zIndex(2f)
          .applyHoloAndDragEffect(),
      )

      Spacer(Modifier.height(16.dp))

      InfoCard(
        name = state.cardName,
        card = state.pokemonCard,
        modifier = Modifier.padding(horizontal = 16.dp),
      )

      state.pokemonCard?.tcgPlayer?.let { tcgPlayer ->
        Spacer(Modifier.height(16.dp))
        TcgPlayerPriceCard(
          tcgPlayer = tcgPlayer,
          onBuyClick = { state.eventSink(CardDetailUiEvent.OpenUrl(tcgPlayer.url)) },
          modifier = Modifier.padding(horizontal = 16.dp),
        )
      }

      state.pokemonCard?.cardMarket?.let { cardMarket ->
        Spacer(Modifier.height(16.dp))
        CardMarketPriceCard(
          cardMarket = cardMarket,
          onBuyClick = { state.eventSink(CardDetailUiEvent.OpenUrl(cardMarket.url)) },
          modifier = Modifier.padding(horizontal = 16.dp),
        )
      }

      Spacer(Modifier.height(16.dp))
    }
  }
}

// TODO: Add better loading state
@Composable
private fun CardImage(
  url: String,
  contentDescription: String,
  modifier: Modifier = Modifier,
) {
  val imageAction by key(url) { rememberImageAction(url) }
  Box(
    modifier = modifier,
  ) {
    Image(
      painter = rememberImageActionPainter(imageAction),
      modifier = Modifier.fillMaxWidth(),
      contentDescription = contentDescription,
      contentScale = ContentScale.FillWidth,
    )

    if (imageAction is ImageEvent) {
      SpinningPokeballLoadingIndicator(
        modifier = Modifier.align(Alignment.Center),
      )
    }
  }
}
