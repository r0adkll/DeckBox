package app.deckbox.playtest.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.core.model.Card
import app.deckbox.playtest.ui.composables.components.PlayMarker
import deckbox.features.playtest.ui.generated.resources.Res
import deckbox.features.playtest.ui.generated.resources.play_mat_stadium
import org.jetbrains.compose.resources.stringResource

@Composable
fun StadiumSpot(
  card: Card?,
  modifier: Modifier = Modifier,
  onClick: (Card) -> Unit = {},
) {
  if (card != null) {
    PokemonCard(
      card = card,
      onClick = { onClick(card) },
      modifier = modifier,
    )
  } else {
    PlayMarker(
      modifier = modifier,
    ) {
      Text(
        text = stringResource(Res.string.play_mat_stadium).uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.align(Alignment.Center),
      )
    }
  }
}
