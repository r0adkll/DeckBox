package app.deckbox.playtest.api.model

import androidx.compose.runtime.Immutable
import app.deckbox.core.extensions.shuffle
import app.deckbox.core.model.Card
import app.deckbox.core.model.SuperType
import kotlin.random.Random
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

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
  val shuffledCards = cards
    .shuffle(7)
    .toMutableList()

  fun draw() = shuffledCards.removeFirst()
  fun drawOf(predicate: (Card) -> Boolean) = shuffledCards.find(predicate).also(shuffledCards::remove)
  fun Card.play() = PlayedCard(
    pokemons = persistentListOf(this),
    energy = (0..1).mapNotNull { drawOf { it.supertype == SuperType.ENERGY } }.toImmutableList(),
    damage = 20,
    isPoisoned = Random.nextBoolean(),
    isBurned = Random.nextBoolean(),
  )

  val prizes = (0 until 6).associateWith { draw() }.toImmutableMap()
  val hand = (0 until 7).map { draw() }.toImmutableList()
  val discard = (0 until 2).map { draw() }.toImmutableList()
  val bench = Bench(
    cards = mapOf(
      0 to drawOf { it.supertype == SuperType.POKEMON }!!.play(),
      1 to drawOf { it.supertype == SuperType.POKEMON }!!.play(),
    ).toImmutableMap(),
  )
  val active = draw().play()

  val player = Player(
    type = Player.Type.PLAYER,
    deck = shuffledCards.toImmutableList(),
    hand = hand,
    discard = discard,
    prizes = prizes,
    bench = bench,
    active = active,
  )

  return Board(
    player = player,
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
    OPPONENT,
  }
}

@Immutable
data class Bench(
  val cards: ImmutableMap<Int, PlayedCard> = persistentMapOf(),
  val size: Int = 7,
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
  PARALYZED,
}
