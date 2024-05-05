package app.deckbox.common.compose.widgets.builder.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.deckbox.common.compose.icons.rounded.AddCard
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.core.model.Card
import app.deckbox.core.model.Stacked
import cafe.adriel.lyricist.LocalStrings

@Composable
internal fun SuggestedCardTip(
  card: Stacked<Card>,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp,
      )
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.tertiary,
        shape = RoundedCornerShape(16.dp),
      )
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(
        horizontal = 16.dp,
        vertical = 16.dp,
      ),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    PokemonCard(
      card = card.card,
      onClick = onClick,
      modifier = Modifier.height(64.dp),
    )
    Spacer(Modifier.width(16.dp))
    Text(
      text = LocalStrings.current.addSuggestedEnergyCards(card.count, card.card.name),
      style = MaterialTheme.typography.labelLarge,
      modifier = Modifier.weight(1f),
    )
    Spacer(Modifier.width(8.dp))
    Icon(
      Icons.Rounded.AddCard,
      contentDescription = null,
    )
  }
}
