package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.deckbox.core.model.stack
import app.deckbox.playtest.ui.model.Bench
import com.slack.circuit.retained.collectAsRetainedState

@Composable
internal fun Bench(
  bench: Bench,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier,
  ) {
    for (index in 0 until bench.size) {
      val card = bench.cards[index]
      if (card != null) {
        // TODO Render
        InPlayCard(
          card = card,
          onClick = {

          }
        )
      } else {
        PlayMarker(
          modifier = Modifier
            // The PlayMarker will force the card aspect ratio and manage it's own width
            .fillMaxHeight()
        )
      }
    }
  }
}
