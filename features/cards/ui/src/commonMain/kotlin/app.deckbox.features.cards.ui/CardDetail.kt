package app.deckbox.features.cards.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.moriatsushi.insetsx.systemBars
import com.r0adkll.kotlininject.merge.annotations.CircuitInject
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@CircuitInject(MergeActivityScope::class, CardDetailScreen::class)
@Composable
internal fun CardDetail(
  state: CardDetailUiState,
  modifier: Modifier = Modifier,
) {
  BottomSheetScaffold(
    sheetContent = {
      Text(
        text = "Bottom Sheet",
        modifier = Modifier.height(300.dp),
      )
    },
    sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    backgroundColor = Color.Black.copy(0.75f),
  ) {
    Box(
      modifier = modifier.fillMaxSize(),
    ) {
      CompositionLocalProvider(
        LocalContentColor provides Color.White,
      ) {
        when (state) {
          CardDetailUiState.Loading -> Loading()
          is CardDetailUiState.Error -> Error()
          is CardDetailUiState.Loaded -> {
            CardDetailContent(
              state = state,
              modifier = Modifier
                .align(Alignment.Center),
            )
          }
        }
      }
    }
  }
}

@Composable
private fun BoxScope.Loading(
  modifier: Modifier = Modifier,
) {
  SpinningPokeballLoadingIndicator(
    size = 56.dp,
    modifier = modifier.align(Alignment.Center),
  )
}

@Composable
private fun BoxScope.Error(
  modifier: Modifier = Modifier,
) {
  Text(
    text = "Uh-oh! Unable to load pokemon card.",
    modifier = modifier
      .align(Alignment.Center)
      .padding(horizontal = 32.dp),
  )
}

@Composable
private fun CardDetailContent(
  state: CardDetailUiState.Loaded,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = Modifier
      .background(Color.Black.copy(0.5f))
      .windowInsetsPadding(
        WindowInsets.systemBars
          .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
      )
      .height(56.dp)
      .fillMaxWidth()
      .zIndex(1f),
    contentAlignment = Alignment.CenterStart,
  ) {
    IconButton(
      onClick = {
        (state as? CardDetailUiState.Loaded)
          ?.eventSink
          ?.invoke(CardDetailUiEvent.NavigateBack)
      },
    ) {
      Icon(Icons.Rounded.Close, contentDescription = null)
    }
  }

  CardImage(
    url = state.card.image.large,
    contentDescription = state.card.name,
    modifier = modifier.fillMaxSize(),
  )
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

  val painter = key(url) { rememberImagePainter(url) }
  Image(
    painter = painter,
    modifier = modifier
      .graphicsLayer(
        scaleX = scale.value,
        scaleY = scale.value,
        rotationZ = rotation.value,
        translationX = offset.value.x,
        translationY = offset.value.y,
      )
      .transformable(state)
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
}
