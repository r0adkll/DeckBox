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

@Immutable
data class Turn(
  val count: Int,
  val whoIs: Player.Type,
)

@Immutable
data class Player(
  val type: Type,
  val hand: ImmutableList<Card>,
  val deck: ImmutableList<Card>,
  val discard: ImmutableList<Card>,
  val lostZone: ImmutableList<Card>,
  val prizes: ImmutableMap<Int, Card>,
  val bench: Bench,
  val active: PlayedCard?,
  val stadium: Card?,
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
