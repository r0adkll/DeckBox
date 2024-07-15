package app.deckbox.playtest.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.deckbox.core.model.Card
import app.deckbox.playtest.ui.composables.components.CardBack
import app.deckbox.playtest.ui.composables.components.MatLabel
import app.deckbox.playtest.ui.composables.components.PlayMarker
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CardPile(
  label: String,
  cards: ImmutableList<Card>,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (cards.isNotEmpty()) {
    CardBack(
      label = label.uppercase(),
      count = cards.size,
      modifier = modifier,
      onClick = onClick,
    )
  } else {
    PlayMarker {
      MatLabel(
        text = label.uppercase(),
        modifier = Modifier.align(Alignment.Center),
      )
    }
  }
}


