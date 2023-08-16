package app.deckbox.features.cards.ui

import DeckBoxAppBar
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import app.deckbox.common.compose.widgets.SpinningPokeballLoadingIndicator
import app.deckbox.common.screens.CardDetailScreen
import app.deckbox.core.di.MergeActivityScope
import app.deckbox.features.cards.ui.composables.CardMarketPriceCard
import app.deckbox.features.cards.ui.composables.InfoCard
import app.deckbox.features.cards.ui.composables.TcgPlayerPriceCard
import com.moriatsushi.insetsx.navigationBars
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(MergeActivityScope::class, CardDetailScreen::class)
@Composable
internal fun CardDetail(
  state: CardDetailUiState,
  modifier: Modifier = Modifier,
) {
  Scaffold(
    modifier = modifier,
    topBar = {
      DeckBoxAppBar(
        title = "",
        navigationIcon = {
          IconButton(
            onClick = { state.eventSink(CardDetailUiEvent.NavigateBack) },
          ) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
          }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
          containerColor = Color.Transparent,
        ),
      )
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
          .zIndex(2f),
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
          modifier = Modifier.padding(horizontal = 16.dp),
        )
      }

      state.pokemonCard?.cardMarket?.let { cardMarket ->
        Spacer(Modifier.height(16.dp))
        CardMarketPriceCard(
          cardMarket = cardMarket,
          modifier = Modifier.padding(horizontal = 16.dp),
        )
      }

      Spacer(Modifier.height(16.dp))
    }
  }
}

@Composable
private fun CardImage(
  url: String,
  contentDescription: String,
  modifier: Modifier = Modifier,
) {
  val coroutineScope = rememberCoroutineScope()

  val scale = remember { Animatable(1f) }
  val rotation = remember { Animatable(0f) }
  val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

  val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
    coroutineScope.launch {
      scale.snapTo((scale.value * zoomChange).coerceIn(0.75f, 2f))
      rotation.snapTo(rotation.value + rotationChange)
      offset.snapTo(offset.value + offsetChange)
    }
  }

  val imageAction by key(url) { rememberImageAction(url) }
  Box(
    modifier = modifier,
  ) {
    Image(
      painter = rememberImageActionPainter(imageAction),
      modifier = Modifier
        .fillMaxWidth()
        .graphicsLayer(
          scaleX = scale.value,
          scaleY = scale.value,
          rotationZ = rotation.value,
          translationX = offset.value.x,
          translationY = offset.value.y,
        )
        .transformable(state, enabled = false)
        .pointerInput(url) {
          coroutineScope {
            awaitPointerEventScope {
              while (true) {
                val event = awaitPointerEvent()
                if (event.type == PointerEventType.Release || event.type == PointerEventType.Exit) {
                  launch {
                    val s = async { scale.animateTo(1f) }
                    val r = async { rotation.animateTo(0f) }
                    val o = async { offset.animateTo(Offset.Zero) }
                    s.await()
                    r.await()
                    o.await()
                  }
                }
              }
            }
          }
        },
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
