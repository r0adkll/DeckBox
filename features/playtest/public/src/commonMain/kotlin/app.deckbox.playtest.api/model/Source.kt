package app.deckbox.playtest.api.model

import app.deckbox.core.model.Card
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

sealed class Source<T, B : Source.Builder<T>> {

  abstract fun apply(player: Player, builder: B.() -> Unit): Player

  data object Active : Source<PlayedCard, CardBuilder>() {

    override fun apply(player: Player, builder: CardBuilder.() -> Unit): Player {
      return player.active?.let {
        val b = CardBuilder(it)
        builder.invoke(b)
        player.copy(active = b.build())
      } ?: player
    }
  }

  class Bench(val position: Int) : Source<PlayedCard, CardBuilder>() {

    override fun apply(player: Player, builder: CardBuilder.() -> Unit): Player {
      val benchItem = player.bench.cards[position]
      return benchItem?.let {
        val b = CardBuilder(it)
        builder.invoke(b)

        val benchItems = player.bench.cards.toMutableMap()
        val newBenchItem = b.build()
        if (newBenchItem == null) {
          benchItems.remove(position)
        } else {
          benchItems[position] = newBenchItem
        }
        val bench = player.bench.copy(cards = benchItems.toImmutableMap())

        player.copy(bench = bench)
      } ?: player
    }
  }

  object Hand : Source<List<Card>, ListBuilder>() {

    override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
      val b = ListBuilder(player.hand)
      builder.invoke(b)
      return player.copy(hand = b.build())
    }
  }

  object Deck : Source<List<Card>, ListBuilder>() {

    override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
      val b = ListBuilder(player.deck.toList())
      builder.invoke(b)
      return player.copy(deck = b.build())
    }
  }

  object Discard : Source<List<Card>, ListBuilder>() {

    override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
      val b = ListBuilder(player.discard)
      builder.invoke(b)
      return player.copy(discard = b.build())
    }
  }

  object LostZone : Source<List<Card>, ListBuilder>() {

    override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
      val b = ListBuilder(player.lostZone)
      builder.invoke(b)
      return player.copy(lostZone = b.build())
    }
  }

  /**
   * An interface for building immutable objects from a mutable state
   */
  interface Builder<T> {

    fun build(): T?
  }

  /**
   * Helper class to make the [PlayedCard] mutable for ease of editing before translating
   * back to immutability
   */
  class CardBuilder(
    var pokemons: ArrayDeque<Card>,
    var energy: MutableList<Card>,
    var tools: MutableList<Card>,
    var isPoisoned: Boolean,
    var isBurned: Boolean,
    var statusEffect: StatusEffect?,
    var damage: Int,
  ) : Builder<PlayedCard> {

    val isEmpty: Boolean
      get() = pokemons.isEmpty() && energy.isEmpty() && tools.isEmpty()

    constructor(card: PlayedCard) : this(
      ArrayDeque(card.pokemons),
      card.energy.toMutableList(),
      card.tools.toMutableList(),
      card.isPoisoned,
      card.isBurned,
      card.statusEffect,
      card.damage,
    )

    /**
     * Build the immutable object with the updated parameters in this obj
     */
    override fun build(): PlayedCard? {
      return if (isEmpty) {
        null
      } else {
        PlayedCard(
          pokemons = pokemons.toImmutableList(),
          energy = energy.toImmutableList(),
          tools = tools.toImmutableList(),
          isPoisoned = isPoisoned,
          isBurned = isBurned,
          statusEffect = statusEffect,
          damage = damage,
        )
      }
    }
  }

  class ListBuilder(items: List<Card>) : Builder<List<Card>> {

    val items = items.toMutableList()

    override fun build(): ImmutableList<Card> {
      return items.toImmutableList()
    }
  }
}
