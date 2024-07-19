package app.deckbox.playtest.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.deckbox.playtest.ui.composables.components.MatLabel
import app.deckbox.playtest.ui.composables.components.PlayMarker
import app.deckbox.playtest.api.model.PlayedCard
import deckbox.features.playtest.ui.generated.resources.Res
import deckbox.features.playtest.ui.generated.resources.play_mat_active
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ActiveSpot(
  card: PlayedCard?,
  onClick: (PlayedCard) -> Unit,
  modifier: Modifier = Modifier,
) {
  if (card != null) {
    InPlayCard(
      card = card,
      onClick = { onClick(card) },
      modifier = modifier,
    )
  } else {
    PlayMarker(
      modifier = modifier,
    ) {
      MatLabel(
        text = stringResource(Res.string.play_mat_active).uppercase(),
        modifier = Modifier.align(Alignment.Center),
      )
    }
  }
}
