package app.deckbox.ui.decks.builder.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CatchingPokemon
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.filled.Cards
import app.deckbox.common.compose.icons.rounded.Energy
import app.deckbox.common.compose.icons.rounded.Wrench

@Composable
internal fun CardCounter(
  total: Int,
  pokemon: Int,
  trainer: Int,
  energy: Int,
  modifier: Modifier = Modifier,
  containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
  contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
) {
  CompositionLocalProvider(
    LocalContentColor provides contentColor,
  ) {
    var isExpanded by remember { mutableStateOf(false) }

    Row(
      modifier = modifier
        .background(
          color = containerColor,
          shape = RoundedCornerShape(50),
        )
        .border(
          width = 1.dp,
          color = contentColor,
          shape = RoundedCornerShape(50),
        )
        .clip(RoundedCornerShape(50))
        .clickable {
          isExpanded = !isExpanded
        }
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
        .animateContentSize(),
    ) {
      if (isExpanded) {
        CardCount(pokemon, Icons.Rounded.CatchingPokemon)
        Spacer(Modifier.width(8.dp))
        CardCount(trainer, Icons.Rounded.Wrench)
        Spacer(Modifier.width(8.dp))
        CardCount(energy, Icons.Rounded.Energy)
        Spacer(Modifier.width(12.dp))
      }
      CardCount(total, Icons.Filled.Cards)
    }
  }
}

@Composable
private fun CardCount(
  count: Int,
  icon: ImageVector,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      icon,
      contentDescription = null,
    )
    Spacer(Modifier.width(4.dp))
    Text(
      text = count.toString(),
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.SemiBold,
    )
  }
}
