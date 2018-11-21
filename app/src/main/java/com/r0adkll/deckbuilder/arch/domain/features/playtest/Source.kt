package com.r0adkll.deckbuilder.arch.domain.features.playtest

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player
import java.util.*


sealed class Source<T, B : Source.Builder<T>> {

    abstract fun apply(player: Player, builder: B.() -> Unit): Player


    object Active : Source<Board.Card, CardBuilder>() {

        override fun apply(player: Player, builder: CardBuilder.() -> Unit): Player {
            return player.active?.let {
                val b = CardBuilder(it)
                builder.invoke(b)
                player.copy(active = b.build())
            } ?: player
        }
    }


    class Bench(val position: Int) : Source<Board.Card, CardBuilder>() {

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
                val bench = player.bench.copy(cards = benchItems)

                player.copy(bench = bench)
            } ?: player
        }
    }


    object Hand : Source<List<PokemonCard>, ListBuilder>() {

        override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
            val b = ListBuilder(player.hand)
            builder.invoke(b)
            return player.copy(hand = b.build())
        }
    }


    object Deck : Source<List<PokemonCard>, ListBuilder>() {

        override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
            val b = ListBuilder(player.deck.toList())
            builder.invoke(b)
            return player.copy(deck = ArrayDeque(b.build()))
        }
    }


    object Discard : Source<List<PokemonCard>, ListBuilder>() {

        override fun apply(player: Player, builder: ListBuilder.() -> Unit): Player {
            val b = ListBuilder(player.discard)
            builder.invoke(b)
            return player.copy(discard = b.build())
        }
    }


    object LostZone : Source<List<PokemonCard>, ListBuilder>() {

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
     * Helper class to make the [Board.Card] mutable for ease of editing before translating
     * back to immutability
     */
    class CardBuilder(
            var pokemons: Stack<PokemonCard>,
            var energy: MutableList<PokemonCard>,
            var tools: List<PokemonCard>,
            var isPoisoned: Boolean,
            var isBurned: Boolean,
            var statusEffect: Board.Card.Status?,
            var damage: Int
    ): Builder<Board.Card> {

        val isEmpty: Boolean
            get() = pokemons.isEmpty() && energy.isEmpty() && tools.isEmpty()

        constructor(card: Board.Card) : this(card.pokemons, card.energy.toMutableList(), card.tools,
                card.isPoisoned, card.isBurned, card.statusEffect, card.damage)

        /**
         * Build the immutable object with the updated parameters in this obj
         */
        override fun build(): Board.Card? {
            return if (isEmpty) {
                null
            } else {
                Board.Card(pokemons, energy, tools, isPoisoned, isBurned, statusEffect, damage)
            }
        }
    }


    class ListBuilder(items: List<PokemonCard>) : Builder<List<PokemonCard>> {

        val items = items.toMutableList()

        override fun build(): List<PokemonCard> {
            return items
        }
    }
}