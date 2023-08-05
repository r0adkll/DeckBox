package app.deckbox.common.compose.widgets

import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.DeckBoxIcons
import app.deckbox.common.compose.icons.GreatBall
import app.deckbox.common.compose.icons.MasterBall
import app.deckbox.common.compose.icons.PokeBall
import app.deckbox.common.compose.icons.UltraBall

@Composable
fun SpinningPokeballLoadingIndicator(
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  pokeball: ImageVector = rememberRandomPokeball(),
) {
  val transition = rememberInfiniteTransition()
  val rotationDegrees by transition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(300, easing = LinearEasing),
      repeatMode = RepeatMode.Restart,
    ),
  )

  Image(
    pokeball,
    contentDescription = null,
    modifier = modifier
      .size(size)
      .rotate(rotationDegrees),
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
