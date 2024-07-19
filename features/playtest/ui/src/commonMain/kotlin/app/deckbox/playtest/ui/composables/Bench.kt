package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.playtest.ui.composables.components.MatLabel
import app.deckbox.playtest.ui.composables.components.PlayMarker
import app.deckbox.playtest.api.model.Bench
import app.deckbox.playtest.api.model.PlayedCard
import deckbox.features.playtest.ui.generated.resources.Res
import deckbox.features.playtest.ui.generated.resources.play_mat_bench
import org.jetbrains.compose.resources.stringResource

internal val BenchSpacing = 4.dp

@Composable
internal fun Bench(
  bench: Bench,
  onClick: (PlayedCard) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .padding(horizontal = BenchSpacing)
      .wrapContentHeight()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(BenchSpacing),
  ) {
    for (index in 0 until bench.size) {
      val card = bench.cards[index]
      if (card != null) {
        InPlayCard(
          card = card,
          onClick = { onClick(card) },
        )
      } else {
        PlayMarker(
          modifier = Modifier
            // The PlayMarker will force the card aspect ratio and manage it's own width
            .fillMaxHeight(),
        ) {
          MatLabel(
            text = stringResource(Res.string.play_mat_bench),
            modifier = Modifier.align(Alignment.Center),
          )
        }
      }
    }
  }
}
