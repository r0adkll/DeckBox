package com.r0adkll.deckbuilder.arch.domain.features.playtest

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * This represents the entire board state of a game
 */
data class Board(
        val player: Player,
        val opponent: Player,
        val turn: Turn
) {

    /**
     * Get the boards active stadium, if exists
     */
    val stadium: PokemonCard?
        get() = player.stadium ?: opponent.stadium


    operator fun get(type: Player.Type): Player = when(type) {
        Player.Type.PLAYER -> player
        Player.Type.OPPONENT -> opponent
    }


    data class Turn(
            val count: Int,
            val whos: Player.Type
    )


    /**
     * Represents a player's board state in the game
     */
    @Parcelize
    data class Player(
            val type: Type,
            val hand: List<PokemonCard>,
            val prizes: Map<Int, PokemonCard>,
            val deck: ArrayDeque<PokemonCard>,
            val discard: List<PokemonCard>,
            val lostZone: List<PokemonCard>,
            val bench: Bench,
            val active: Card?,
            val stadium: PokemonCard?
    ) : Parcelable {

        enum class Type {
            PLAYER,
            OPPONENT
        }
    }

    /**
     * Represents a bench state on the board with a default size of 5, and possible expansion
     * of up to 8 via SkyField
     */
    @Parcelize
    data class Bench(
            val cards: Map<Int, Card> = HashMap(),
            val size: Int = 5
    ) : Parcelable


    /**
     * Represents a card on the board, including it's evolutions, energy, tools, status effects,
     * damage, and so on
     */
    @Parcelize
    data class Card(
            val pokemons: ArrayDeque<PokemonCard>,
            val energy: List<PokemonCard> = emptyList(),
            val tools: List<PokemonCard> = emptyList(),
            val isPoisoned: Boolean = false,
            val isBurned: Boolean = false,
            val statusEffect: Status? = null,
            val damage: Int = 0
    ): Parcelable {

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
