package app.deckbox.common.compose.widgets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Card
import com.seiko.imageloader.model.ImageEvent
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

internal val CardCornerRadius = 8.dp
const val CardAspectRatio = 0.7167969f

@Composable
fun PokemonCard(
  card: Card,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val imageAction by key(card.id) {
    rememberImageAction(card.image.small)
  }

  TradingCard(
    onClick = onClick,
    modifier = modifier,
  ) {
    Image(
      painter = rememberImageActionPainter(imageAction),
      contentDescription = card.name,
      modifier = Modifier.fillMaxSize(),
    )

    if (imageAction is ImageEvent) {
      val transition = rememberInfiniteTransition()

      val alpha by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
          animation = tween(500),
          repeatMode = RepeatMode.Reverse,
        ),
      )

      PokemonCardBack(
        modifier = Modifier
          .fillMaxSize()
          .alpha(alpha),
      )
    }
  }
}

@Composable
fun ShimmerPokemonCard(
  modifier: Modifier = Modifier,
) {
  TradingCard(
    enabled = false,
    modifier = modifier,
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray)
        .shimmer(),
    )
  }
}

@Composable
private fun TradingCard(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  onClick: () -> Unit = {},
  content: @Composable BoxScope.() -> Unit,
) {
  val shape = RoundedCornerShape(CardCornerRadius)
  Box(
    modifier = modifier
      .aspectRatio(CardAspectRatio)
      .wrapContentSize()
      .clip(shape)
      .clickable(
        role = Role.Image,
        enabled = enabled,
        onClick = onClick,
      ),
  ) {
    content()
  }
}
