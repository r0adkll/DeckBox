package com.r0adkll.deckbuilder.arch.domain.features.playtest

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import java.util.*


/**
 * This represents the entire board state of a game
 */
class Board(
        var player: Player,
        var opponent: Player,
        var turn: Turn
) {

    /**
     * Get the boards active stadium, if exists
     */
    val stadium: Card?
        get() = player.stadium ?: opponent.stadium


    class Turn(
            val count: Int,
            val whos: Player.Type
    )


    /**
     * Represents a player's board state in the game
     */
    class Player(
            val hand: List<PokemonCard>,
            val prizes: List<PokemonCard>,
            val deck: List<PokemonCard>,
            val discard: List<PokemonCard>,
            val lostZone: List<PokemonCard>,
            val bench: Bench,
            val active: Card?,
            val stadium: Card?
    ) {

        enum class Type {
            PLAYER,
            OPPONENT
        }
    }

    /**
     * Represents a bench state on the board with a default size of 5, and possible expansion
     * of up to 8 via SkyField
     */
    class Bench(
            val cards: Array<Card?> = Array(8) { null },
            val size: Int = 5
    )


    /**
     * Represents a card on the board, including it's evolutions, energy, tools, status effects,
     * damage, and so on
     */
    class Card(
            val pokemons: Stack<PokemonCard>,
            val energy: List<PokemonCard>,
            val tool: PokemonCard?,
            val isPoisoned: Boolean,
            val isBurned: Boolean,
            val statusEffect: Status?,
            val damage: Int
    ) {

        /**
         * Conditional status effects
         */
        enum class Status {
            CONFUSED,
            SLEEPING,
            PARALYZED
        }
    }
}