package com.r0adkll.deckbuilder.arch.domain.features.playtest

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player
import java.util.*


sealed class Source {

    abstract fun apply(player: Player, builder: CardBuilder.() -> Unit): Player


    object Active : Source() {

        override fun apply(player: Player, builder: CardBuilder.() -> Unit): Player {
            return player.active?.let {
                val b = CardBuilder(it)
                builder.invoke(b)
                player.copy(active = b.build())
            } ?: player
        }
    }


    class Bench(val position: Int) : Source() {

        override fun apply(player: Player, builder: CardBuilder.() -> Unit): Player {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }


    /**
     * Helper class to make the [Board.Card] mutable for ease of editing before translating
     * back to immutability
     */
    class CardBuilder(
            var pokemons: Stack<PokemonCard>,
            var energy: MutableList<PokemonCard>,
            var tool: PokemonCard?,
            var isPoisoned: Boolean,
            var isBurned: Boolean,
            var statusEffect: Board.Card.Status?,
            var damage: Int
    ) {

        constructor(card: Board.Card) : this(card.pokemons, card.energy.toMutableList(), card.tool,
                card.isPoisoned, card.isBurned, card.statusEffect, card.damage)

        /**
         * Build the immutable object with the updated parameters in this obj
         */
        fun build(): Board.Card {
            return Board.Card(pokemons, energy, tool, isPoisoned, isBurned, statusEffect, damage)
        }
    }
}