package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.playtest.ui.model.Bench

// FIXME: TEMPORARY! SHOULD NOT KEEP THIS
val PlayCardWidth = 50.dp

@Composable
internal fun Bench(
  bench: Bench,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .wrapContentHeight(),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    for (index in 0 until bench.size) {
      val card = bench.cards[index]
      if (card != null) {
        // TODO Render
        InPlayCard(
          modifier = Modifier.weight(1f),
          card = card,
          onClick = {
          },
        )
      } else {
        PlayMarker(
          modifier = Modifier
//            .width(PlayCardWidth)
            .weight(1f)
            // The PlayMarker will force the card aspect ratio and manage it's own width
            .fillMaxHeight(),
        )
      }
    }
  }
}
