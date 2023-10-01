package app.deckbox.playtest.ui.model

import androidx.compose.runtime.Immutable
import app.deckbox.core.model.Card
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class Board(
  val player: Player,
  val opponent: Player,
  val turn: Turn,
) {

  val stadium: Card?
    get() = player.stadium ?: opponent.stadium

  val currentPlayer: Player
    get() = this[turn.whoIs]

  operator fun get(type: Player.Type): Player = when (type) {
    Player.Type.PLAYER -> player
    Player.Type.OPPONENT -> opponent
  }
}

fun newGame(cards: ImmutableList<Card>): Board {
  return Board(
    player = Player(
      type = Player.Type.PLAYER,
      deck = cards,
    ),
    opponent = Player(
      type = Player.Type.OPPONENT,
      deck = cards, // TODO: Factor in opponent here
    ),
    turn = Turn(),
  )
}

@Immutable
data class Turn(
  val count: Int = 0,
  val whoIs: Player.Type = Player.Type.PLAYER,
)

@Immutable
data class Player(
  val type: Type,
  val hand: ImmutableList<Card> = persistentListOf(),
  val deck: ImmutableList<Card>,
  val discard: ImmutableList<Card> = persistentListOf(),
  val lostZone: ImmutableList<Card> = persistentListOf(),
  val prizes: ImmutableMap<Int, Card> = persistentMapOf(),
  val bench: Bench = Bench(),
  val active: PlayedCard? = null,
  val stadium: Card? = null,
) {
  enum class Type {
    PLAYER,
    OPPONENT
  }
}

@Immutable
data class Bench(
  val cards: ImmutableMap<Int, PlayedCard> = persistentMapOf(),
  val size: Int = 5,
)

@Immutable
data class PlayedCard(
  val pokemons: ImmutableList<Card>,
  val energy: ImmutableList<Card> = persistentListOf(),
  val tools: ImmutableList<Card> = persistentListOf(),
  val isPoisoned: Boolean = false,
  val isBurned: Boolean = false,
  val statusEffect: StatusEffect? = null,
  val damage: Int = 0,
)

enum class StatusEffect {
  CONFUSED,
  SLEEPING,
  PARALYZED
}
