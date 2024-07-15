package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMaxBy
import androidx.compose.ui.util.fastSumBy
import app.deckbox.common.compose.widgets.CardAspectRatio
import app.deckbox.common.compose.widgets.CardCornerRadius
import app.deckbox.common.compose.widgets.CardCounter
import app.deckbox.common.compose.widgets.PokemonCard
import app.deckbox.common.compose.widgets.PokemonCardBack
import app.deckbox.core.model.Card
import app.deckbox.playtest.ui.composables.components.MatLabel
import app.deckbox.playtest.ui.composables.components.PlayMarker
import deckbox.features.playtest.ui.generated.resources.Res
import deckbox.features.playtest.ui.generated.resources.play_mat_active
import deckbox.features.playtest.ui.generated.resources.play_mat_prizes
import kotlinx.collections.immutable.ImmutableMap
import org.jetbrains.compose.resources.stringResource

val DefaultCardPriceOffset = 5.dp

@Composable
internal fun Prizes(
  prizes: ImmutableMap<Int, Card>,
  onClick: (Map<Int, Offset>) -> Unit,
  modifier: Modifier = Modifier,
  cardSpacing: Dp = DefaultCardPriceOffset,
) {
  Box(
    modifier = modifier
      .padding(bottom = DefaultCardPriceOffset * (prizes.size - 1).coerceAtLeast(0)),
  ) {
    val globalPositioning = remember { mutableMapOf<Int, Offset>() }
    prizes.entries.forEachIndexed { index, (position, _) ->
      PriceCard(
        count = prizes.size.takeIf { index == prizes.size - 1 },
        onClick = {
          onClick(globalPositioning)
        },
        modifier = Modifier
          .offset(
            x = 0.dp,
            y = cardSpacing * index,
          )
          .onGloballyPositioned {
            globalPositioning[position] = it.localToRoot(Offset.Zero)
          },
      )
    }

    if (prizes.entries.isEmpty()) {
      PlayMarker {
        MatLabel(
          text = stringResource(Res.string.play_mat_prizes),
          modifier = Modifier.align(Alignment.Center),
        )
      }
    }
  }
}

@Composable
private fun PriceCard(
  count: Int?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val cardWidth = LocalArenaCardWidth.current
  Box(
    modifier = modifier
      .width(cardWidth)
      .aspectRatio(CardAspectRatio)
      .clickable(onClick = onClick, enabled = count != null)
      .clip(RoundedCornerShape(CardCornerRadius)),
  ) {
    PokemonCardBack(
      modifier = Modifier.fillMaxSize(),
    )

    CardCounter(
      count = count,
      collected = null,
      modifier = Modifier.align(Alignment.BottomStart),
    )
  }
}
