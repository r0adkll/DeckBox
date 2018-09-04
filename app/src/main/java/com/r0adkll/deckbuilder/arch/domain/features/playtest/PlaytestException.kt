package com.r0adkll.deckbuilder.arch.domain.features.playtest



sealed class PlaytestException(message: String? = null) : Exception(message) {

    /**
     * This exception occurs when the player draws (via turn draw or other) and there are no more cards in the deck
     */
    class DeckOutException : PlaytestException()
}