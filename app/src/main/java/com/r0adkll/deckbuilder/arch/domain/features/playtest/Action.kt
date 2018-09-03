package com.r0adkll.deckbuilder.arch.domain.features.playtest


/**
 * A defined set of actions that can modify the board/player states
 * Extend to define such actions as: Shuffling deck. Attacks. Switching Active. etc
 */
sealed class Action {

    abstract fun apply(board: Board): Board


}