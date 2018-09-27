package com.r0adkll.deckbuilder.arch.domain.features.playtest

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player
import com.r0adkll.deckbuilder.util.extensions.shuffle


/**
 * Defines a part of the [Board]'s [Board.Player] state to add a list of cards that have been
 * removed from another part of the state via UI action that let's the user specify one of these
 * targets to remove the cards to
 */
sealed class Target {

    /**
     * apply the list of cards to the player target
     */
    abstract fun apply(player: Player, cards: List<PokemonCard>): Player


    fun apply(player: Player, vararg cards: PokemonCard): Player = apply(player, cards.asList())


    object Hand : Target() {

        override fun apply(player: Player, cards: List<PokemonCard>): Player {
            return player.copy(hand = cards.plus(player.hand))
        }
    }


    object Deck : Target() {

        override fun apply(player: Player, cards: List<PokemonCard>): Player {
            val deck = player.deck
            deck.addAll(cards)
            return player.copy(deck = deck.shuffle(2))
        }
    }


    object Discard : Target() {

        override fun apply(player: Player, cards: List<PokemonCard>): Player {
            return player.copy(discard = player.discard.plus(cards))
        }
    }


    object LostZone : Target() {

        override fun apply(player: Player, cards: List<PokemonCard>): Player {
            return player.copy(lostZone = player.lostZone.plus(cards))
        }
    }
}