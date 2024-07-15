package app.deckbox.playtest.ui.composables.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.extensions.thenIf
import app.deckbox.common.compose.widgets.CardAspectRatio
import app.deckbox.common.compose.widgets.CardCornerRadius
import app.deckbox.common.compose.widgets.CardCounter
import app.deckbox.common.compose.widgets.PokemonCardBack
import app.deckbox.playtest.ui.composables.LocalArenaCardWidth

@Composable
internal fun CardBack(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  label: String? = null,
  count: Int? = null,
  isSelected: Boolean = false,
) {
  val cardWidth = LocalArenaCardWidth.current
  Box(
    modifier = modifier
      .width(cardWidth)
      .aspectRatio(CardAspectRatio)
      .clickable(onClick = onClick)
      .clip(RoundedCornerShape(CardCornerRadius))
      .thenIf(isSelected) {
        border(
          width = 2.dp,
          color = MaterialTheme.colorScheme.tertiary,
          shape = RoundedCornerShape(CardCornerRadius),
        )
      },
  ) {
    PokemonCardBack(
      modifier = Modifier.fillMaxSize(),
    )

    if (label != null) {
      MatLabel(
        text = label,
        modifier = Modifier
          .align(Alignment.TopCenter)
          .padding(top = 16.dp),
        color = Color.White,
      )
    }

    CardCounter(
      count = count,
      collected = null,
      modifier = Modifier.align(Alignment.BottomStart),
    )
  }
}
