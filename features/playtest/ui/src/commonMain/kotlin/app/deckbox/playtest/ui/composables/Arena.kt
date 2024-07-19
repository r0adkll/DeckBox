package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.deckbox.playtest.api.model.Board
import com.moriatsushi.insetsx.statusBars

/**
 * The Arena is the entire game area responsible for rendering the [board] to the user. This contains
 * the play mat for both the player and the opponent.
 *
 * @param board The board game state to render
 */
@Composable
internal fun Arena(
  board: Board,
  modifier: Modifier = Modifier,
) {
  BoxWithConstraints(
    modifier = modifier
      .fillMaxSize()
  ) {
    // Use MaxWidth of the box here to compute the expected with of a card for the
    // entire arena. This value will then be propagated down to all child elements
    // to be used to render all cards
    val arenaCardWidth = (maxWidth - BenchSpacing * 6) / 5

    CompositionLocalProvider(
      LocalArenaCardWidth provides arenaCardWidth
    ) {
      Column(
        modifier = modifier.fillMaxSize(),
      ) {
        Spacer(
          modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars),
        )

        // Opponent
        PlayMat(
          modifier = Modifier.weight(1f),
          player = board.opponent,
          onActiveClick = {},
          onStadiumClick = {},
          onBenchClick = {},
          onPrizesClick = {},
          onDeckClick = {},
          onDiscardClick = {},
          onLostZoneClick = {},
        )

        // Player
        PlayMat(
          modifier = Modifier.weight(1f),
          player = board.player,
          onActiveClick = {},
          onStadiumClick = {},
          onBenchClick = {},
          onPrizesClick = {},
          onDeckClick = {},
          onDiscardClick = {},
          onLostZoneClick = {},
        )

        Spacer(Modifier.height(16.dp))

        PlayerBar(
          player = board.player,
          onHandClick = {

          },
          navigationIcon = {
            IconButton(
              onClick = {},
            ) {
              Icon(
                Icons.Rounded.Close,
                contentDescription = null,
              )
            }
          }
        )
      }
    }
  }
}

/**
 * Composition local to propagate the uniform width of cards in a given
 * Arena.
 */
val LocalArenaCardWidth = compositionLocalOf { 0.dp }
