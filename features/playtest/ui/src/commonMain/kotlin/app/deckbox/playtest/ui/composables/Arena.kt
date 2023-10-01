package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.playtest.ui.model.Board
import app.deckbox.playtest.ui.model.Player

@Suppress("UNUSED_PARAMETER")
@Composable
internal fun Arena(
  board: Board,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.fillMaxSize(),
  ) {
    Spacer(Modifier.weight(1f))
    Bench(
      bench = board[Player.Type.PLAYER].bench,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
    )
    Spacer(
      modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars),
    )
  }
}
