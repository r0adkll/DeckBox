package app.deckbox.playtest.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.deckbox.core.model.Card
import app.deckbox.playtest.ui.model.PlayedCard
import app.deckbox.playtest.ui.model.Player
import deckbox.features.playtest.ui.generated.resources.Res
import deckbox.features.playtest.ui.generated.resources.play_mat_deck
import deckbox.features.playtest.ui.generated.resources.play_mat_discard
import deckbox.features.playtest.ui.generated.resources.play_mat_lost_zone
import org.jetbrains.compose.resources.stringResource

/**
 * A play mat represents one side of the arena that contains the [player] card setup for simulating
 * a game.
 *
 * This includes:
 * * Active Spot
 * * Stadium
 * * Bench
 * * Prizes
 * * Deck
 * * _Hand_ - This might be part of a separate element, so its inclusion here is TBD
 * * Discard
 * * Lost Zone
 *
 * @param player The player game state information to render their mat
 * @param modifier The composable element modifier for this composable
 */
@Composable
internal fun PlayMat(
  player: Player,
  onActiveClick: (PlayedCard) -> Unit,
  onStadiumClick: (Card) -> Unit,
  onBenchClick: (PlayedCard) -> Unit,
  onPrizesClick: (Map<Int, Offset>) -> Unit,
  onDeckClick: () -> Unit,
  onDiscardClick: () -> Unit,
  onLostZoneClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Layout(
    modifier = modifier,
    content = {
      ActiveSpot(
        card = player.active,
        onClick = onActiveClick,
      )

      StadiumSpot(
        card = player.stadium,
        onClick = onStadiumClick,
      )

      Bench(
        bench = player.bench,
        onClick = onBenchClick,
      )

      Prizes(
        prizes = player.prizes,
        onClick = onPrizesClick,
      )

      // Deck
      CardPile(
        label = stringResource(Res.string.play_mat_deck),
        cards = player.deck,
        onClick = onDeckClick,
      )

      // Discard
      CardPile(
        label = stringResource(Res.string.play_mat_discard),
        cards = player.discard,
        onClick = onDiscardClick,
      )

      // LostZone
      CardPile(
        label = stringResource(Res.string.play_mat_lost_zone),
        cards = player.lostZone,
        onClick = onLostZoneClick,
      )
    },
  ) { measurables: List<Measurable>, constraints ->
    val adjustedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
    val placeables: List<Placeable> = measurables.map { it.measure(adjustedConstraints) }
    val scope = PlayMatScope(placeables, constraints, this)
    with(scope) {
      // Active Spot
      val activeSpot = placeables[ActiveIndex]
      val activePlacement = with(ActiveSpotPlacer) { place(player.type) }

      // Stadium
      val stadiumSpot = placeables[StadiumIndex]
      val stadiumPlacement = with(StadiumSpotPlacer) { place(player.type) }

      // Bench
      val bench = placeables[BenchIndex]
      val benchPlacement = with(BenchPlacer) { place(player.type) }

      // Prizes
      val prizes = placeables[PrizesIndex]
      val prizesPlacement = with(PrizesPlacer) { place(player.type) }

      // Discard
      val discard = placeables[DiscardIndex]
      val discardPlacement = with(DiscardPlacer) { place(player.type) }

      // Deck
      val deck = placeables[DeckIndex]
      val deckPlacement = with(DeckPlacer) { place(player.type) }

      // LostZone
      val lostZone = placeables[LostZoneIndex]
      val lostZonePlacement = with(LostZonePlacer) { place(player.type) }

      layout(constraints.maxWidth, constraints.maxHeight) {
        activeSpot.place(activePlacement)
        stadiumSpot.place(stadiumPlacement)
        bench.place(benchPlacement)
        prizes.place(prizesPlacement)
        discard.place(discardPlacement)
        deck.place(deckPlacement)
        lostZone.place(lostZonePlacement)
      }
    }
  }
}

class PlayMatScope(
  val placeables: List<Placeable>,
  val constraints: Constraints,
  density: Density,
  private val padding: Dp = ContentPadding
) : Density by density {
  val contentPadding: Int get() = padding.roundToPx()

  val active: Placeable get() = placeables[ActiveIndex]
  val stadium: Placeable get() = placeables[StadiumIndex]
  val bench: Placeable get() = placeables[BenchIndex]
  val prizes: Placeable get() = placeables[PrizesIndex]
  val deck: Placeable get() = placeables[DeckIndex]
  val discard: Placeable get() = placeables[DiscardIndex]
  val lostZone: Placeable get() = placeables[LostZoneIndex]
}

interface MatPlacer {
  fun PlayMatScope.placePlayer(): IntOffset
  fun PlayMatScope.placeOpponent(): IntOffset

  fun PlayMatScope.place(
    type: Player.Type
  ) : IntOffset = when (type) {
    Player.Type.PLAYER -> placePlayer()
    Player.Type.OPPONENT -> placeOpponent()
  }
}

fun matPlacer(
  player: PlayMatScope.() -> IntOffset,
  opponent: PlayMatScope.() -> IntOffset,
) = object : MatPlacer {
  override fun PlayMatScope.placePlayer(): IntOffset = player()
  override fun PlayMatScope.placeOpponent(): IntOffset = opponent()
}

private val ActivePadding = 4.dp
private val ActiveSpotPlacer = matPlacer(
  player = {
    IntOffset(
      x = constraints.maxWidth / 2 - active.width / 2,
      y = ActivePadding.roundToPx(),
    )
  },
  opponent = {
    IntOffset(
      x = constraints.maxWidth / 2 - active.width / 2,
      y = constraints.maxHeight - active.height - ActivePadding.roundToPx(),
    )
  },
)

private val StadiumSpotPlacer = matPlacer(
  player = {
    IntOffset(
      x = (constraints.maxWidth / 4) - (stadium.width / 2),
      y = -stadium.height / 2,
    )
  },
  opponent = {
    IntOffset(
      x = (constraints.maxWidth / 4) - (stadium.width / 2),
      y = constraints.maxHeight - stadium.height / 2,
    )
  },
)

private val BenchPlacer = matPlacer(
  player = {
    IntOffset(
      x = 0,
      y = constraints.maxHeight - bench.height,
    )
  },
  opponent = {
    IntOffset(
      x = 0,
      y = 0,
    )
  },
)

private val PrizesMarginHorz = 8.dp
private val PrizesPlacer = matPlacer(
  player = {
    IntOffset(
      x = PrizesMarginHorz.roundToPx(),
      y = constraints.maxHeight - bench.height - contentPadding - prizes.height,
    )
  },
  opponent = {
    IntOffset(
      x = constraints.maxWidth - prizes.width - PrizesMarginHorz.roundToPx(),
      y = bench.height + contentPadding,
    )
  },
)


private val ContentPadding = 8.dp
private val DiscardPlacer = matPlacer(
  player = {
    IntOffset(
      x = constraints.maxWidth - lostZone.width - contentPadding - discard.width - contentPadding,
      y = constraints.maxHeight - discard.height - bench.height - contentPadding,
    )
  },
  opponent = {
    IntOffset(
      x = contentPadding + lostZone.width + contentPadding,
      y = bench.height + contentPadding,
    )
  }
)

private val DeckPlacer = matPlacer(
  player = {
    IntOffset(
      x = constraints.maxWidth - deck.width - discard.width - lostZone.width - (contentPadding * 3),
      y = constraints.maxHeight - deck.height - bench.height - contentPadding,
    )
  },
  opponent = {
    IntOffset(
      x = contentPadding + discard.width + contentPadding + lostZone.width + contentPadding,
      y = bench.height + contentPadding,
    )
  },
)

private val LostZonePlacer = matPlacer(
  player = {
    IntOffset(
      x = constraints.maxWidth - lostZone.width - contentPadding,
      y = constraints.maxHeight - bench.height - contentPadding - lostZone.height,
    )
  },
  opponent = {
    IntOffset(
      x = contentPadding,
      y = bench.height + contentPadding,
    )
  }
)

/*
 * These are easy access indexes for fetching the measurables/placeables in the above Layout
 * for measuring and placing the elements of the play mat.
 *
 * The order of these MUST align with the order of Composables in the above
 * [content] block of the [Layout]
 */
private const val ActiveIndex = 0
private const val StadiumIndex = 1
private const val BenchIndex = 2
private const val PrizesIndex = 3
private const val DeckIndex = 4
private const val DiscardIndex = 5
private const val LostZoneIndex = 6
