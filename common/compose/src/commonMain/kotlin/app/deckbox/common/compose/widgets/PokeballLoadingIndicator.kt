package app.deckbox.common.compose.widgets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.GreatBall
import app.deckbox.common.compose.icons.MasterBall
import app.deckbox.common.compose.icons.PokeBall
import app.deckbox.common.compose.icons.UltraBall

private const val WiggleDegrees = 20f

@Composable
fun PokeballLoadingIndicator(
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  pokeball: ImageVector = rememberRandomPokeball(),
) {
  val transition = rememberInfiniteTransition()
  val rotation by transition.animateFloat(
    initialValue = -WiggleDegrees,
    targetValue = WiggleDegrees,
    animationSpec = infiniteRepeatable(
      animation = tween(300),
      repeatMode = RepeatMode.Reverse,
    ),
  )

  Image(
    pokeball,
    contentDescription = null,
    modifier = modifier
      .size(size)
      .graphicsLayer(
        transformOrigin = TransformOrigin(0.5f, 0.7f),
        rotationZ = rotation,
      ),
  )
}

@Composable
private fun rememberRandomPokeball(): ImageVector {
  return remember {
    listOf(
      DeckBoxIcons.PokeBall,
      DeckBoxIcons.GreatBall,
      DeckBoxIcons.UltraBall,
      DeckBoxIcons.MasterBall,
    ).random()
  }
}
