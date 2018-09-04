package com.r0adkll.deckbuilder.arch.domain.features.playtest


/**
 * A defined set of actions that can modify the board/player states
 * Extend to define such actions as: Shuffling deck. Attacks. Switching Active. etc
 */
sealed class Action {

    abstract fun apply(board: Board): Board


    /**
     * Draw a single card off the deck as per the rules for each players turn
     */
    class TurnDraw(val player: Board.Player.Type) : Action() {

        override fun apply(board: Board): Board = when(player) {
            Board.Player.Type.PLAYER -> board.copy(player = turnDrawCard(board.player))
            Board.Player.Type.OPPONENT -> board.copy(opponent = turnDrawCard(board.opponent))
        }

        private fun turnDrawCard(player: Board.Player): Board.Player {
            if (player.deck.isEmpty()) {
                throw PlaytestException.DeckOutException()
            } else {
                val deck = player.deck
                val hand = player.hand.toMutableList()
                hand.add(deck.pollFirst())
                return player.copy(deck = deck, hand = hand)
            }
        }
    }


    /**
     * A subsection of actions for supporter cards. This is so that we can easily detect that
     * a user has used a supporter card for their turn
     */
    sealed class SupporterAction(val player: Board.Player.Type) : Action() {

        
        class Draw(player: Board.Player.Type, val count: Int) : SupporterAction(player) {

            override fun apply(board: Board): Board {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }


        class ShuffleDraw(player: Board.Player.Type, val count: Int) : SupporterAction(player) {

            override fun apply(board: Board): Board {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}