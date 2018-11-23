package com.r0adkll.deckbuilder.arch.domain.features.playtest

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player
import com.r0adkll.deckbuilder.util.extensions.shuffle
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import java.util.*


/**
 * A defined set of actions that can modify the board/player states
 * Extend to define such actions as: Shuffling deck. Attacks. Switching Active. etc
 */
sealed class Action {

    abstract fun apply(board: Board): Board

    /**
     * Action group that denotes all subclassed actions act upon a player selection as defined
     * by it's constructor parameter [player]
     */
    sealed class Actor(val player: Player.Type) : Action() {

        /**
         * Apply the action to the specified [actor] and return it's updated state
         * @param board the current board state before the action is applied
         * @param actor the player actor that is to be applied to
         */
        abstract fun apply(board: Board, actor: Player): Player


        override fun apply(board: Board): Board {
            val actor = board[player]
            return when(player) {
                Player.Type.PLAYER -> board.copy(player = apply(board, actor))
                Player.Type.OPPONENT -> board.copy(opponent = apply(board, actor))
            }
        }

        /**
         * Draw a single card off the deck as per the rules for each players turn
         */
        class TurnDraw(player: Player.Type) : Actor(player) {

            override fun apply(board: Board, actor: Player): Player {
                if (actor.deck.isEmpty()) {
                    throw PlaytestException.DeckOutException()
                } else {
                    val deck = actor.deck
                    val hand = actor.hand.toMutableList()
                    hand.add(deck.pollFirst())
                    return actor.copy(deck = deck, hand = hand)
                }
            }
        }


        /**
         * Apply an amount of damage to the [player]s active card.
         * @param amount the amount of damage to inflict
         */
        class Damage(player: Player.Type, val source: Source<Board.Card, Source.CardBuilder>, val amount: Int) : Actor(player) {

            override fun apply(board: Board, actor: Player): Player {
                return source.apply(actor) {
                    damage += amount
                }
            }
        }


        /**
         * Attach a [PokemonCard] from the [player]'s hand to the [player]'s active
         * pokemon.
         *
         * For the various type of card attaching, this will attach it appropriately
         * * ENERGY -> adds it to it's attached energy list
         * * TRAINER -> if card is a tool, set's as the cards attached tool card
         * * POKEMON -> if card is the correct evolution of the card on the top of the stack it evolves the card
         *
         * @param card the card from the [player]'s hand to attach
         */
        class Attach(player: Player.Type, val source: Source<Board.Card, Source.CardBuilder>, val card: PokemonCard) : Actor(player) {

            override fun apply(board: Board, actor: Player): Player {
                return source.apply(actor) {
                    if (attach(this, card)) {
                        val hand = actor.hand.toMutableList()
                        if (hand.remove(card)) { // If we can successfully remove the card from our hand
                            actor.copy(hand = hand)
                        }
                    }
                }
            }


            private fun attach(source: Source.CardBuilder, card: PokemonCard): Boolean = when(card.supertype) {
                SuperType.ENERGY -> {
                    source.energy.add(card)
                    true
                }
                SuperType.TRAINER -> when(card.subtype) {
                    SubType.POKEMON_TOOL -> {
                        source.tools.add(card)
                        true
                    }
                    else -> false
                }
                SuperType.POKEMON -> {
                    val topCard = source.pokemons.peek()
                    if (topCard.name == card.evolvesFrom) {
                        source.pokemons.push(card)
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        }


        /**
         * Detach a [PokemonCard] form the [player]'s active card to the specified [target]
         *
         * @param card the card to detach from the active board position
         * @param target the target of where to detach the card to
         */
        class Detach(
                player: Player.Type,
                val source: Source<Board.Card, Source.CardBuilder>,
                val target: Source<List<PokemonCard>, Source.ListBuilder>,
                val card: PokemonCard
        ) : Actor(player) {

            override fun apply(board: Board, actor: Player): Player {
                return source.apply(actor) {
                    when(card.supertype) {
                        SuperType.ENERGY -> {
                            if (energy.remove(card)) { // If we can remove it from our list of energies, yay
                                target.apply(actor) { items.add(card) }
                            }
                        }
                        SuperType.TRAINER -> {
                            if (tools.remove(card)) {
                                target.apply(actor) { items.add(card) }
                            }
                        }
                        SuperType.POKEMON -> {
                            if (pokemons.remove(card)) {
                                target.apply(actor) { items.add(card) }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }


        /**
         * Discard a [Board.Card] and all attachments into the [target]
         * @param source the [Board.Card] source on the [Player] object to take action upon
         * @param target the [PokemonCard] list target on the [Player] object to discard to
         */
        class Discard(
                player: Player.Type,
                val source: Source<Board.Card, Source.CardBuilder>,
                val target: Source<List<PokemonCard>, Source.ListBuilder>
        ) : Actor(player) {

            override fun apply(board: Board, actor: Player): Player {
                return source.apply(actor) {
                    val cardsToDiscard = ArrayList<PokemonCard>()

                    // Discard all pokemon/evolutions
                    cardsToDiscard += pokemons
                    pokemons.clear()

                    // Discard energy
                    cardsToDiscard += energy
                    energy.clear()

                    // Discard tool
                    cardsToDiscard += tools
                    tools.clear()

                    // Now add cards to target
                    target.apply(actor) {
                        items += cardsToDiscard
                    }
                }
            }
        }


        /**
         * Action Group for Hand based actions
         */
        sealed class Hand(player: Player.Type) : Actor(player) {

            /**
             * Drop a [card] from the [player]'s hand into the [target]
             * @param target the player source target to drop the card into
             * @param card the card to remove from the player's hand and drop into the target
             */
            class Drop(player: Player.Type, val target: Source<List<PokemonCard>, Source.ListBuilder>, val card: PokemonCard) : Hand(player) {

                override fun apply(board: Board, actor: Player): Player {
                    val hand = actor.hand.minus(card)
                    val updatedActor = actor.copy(hand = hand)
                    return target.apply(updatedActor) {
                        items.add(card)
                    }
                }
            }


            /**
             * Play a stadium card from the [player]'s hand
             * @param stadiumCard the Stadium card to play
             */
            class Stadium(player: Player.Type, val stadiumCard: PokemonCard) : Hand(player) {

                private val otherPlayer: Player.Type
                    get() = when(player) {
                        Player.Type.PLAYER -> Player.Type.OPPONENT
                        Player.Type.OPPONENT -> Player.Type.PLAYER
                    }

                override fun apply(board: Board): Board {
                    val actor = board[player]
                    val otherActor = board[otherPlayer]
                    if (otherActor.stadium == null || otherActor.stadium.name != stadiumCard.name) {

                        // Cause the other stadium to be discarded
                        val updatedOtherActor = otherActor.stadium?.let {
                            otherActor.copy(stadium = null, discard = otherActor.discard.plus(otherActor.stadium))
                        }

                        val hand = actor.hand.toMutableList()
                        return if (hand.remove(stadiumCard)) {
                            val updatedActor = actor.copy(hand = hand, stadium = stadiumCard)
                            updatedBoard(board, updatedActor, updatedOtherActor ?: otherActor)
                        } else {
                            board
                        }
                    } else {
                        return board
                    }
                }


                // Not used
                override fun apply(board: Board, actor: Player): Player = actor

                private fun updatedBoard(board: Board, actor: Player, otherActor: Player): Board {
                    return board.copy(
                            player = if (player == Player.Type.PLAYER) actor else otherActor,
                            opponent = if (player == Player.Type.OPPONENT) actor else otherActor
                    )
                }
            }
        }


        /**
         * Action Group for Deck-Based actions
         */
        sealed class Deck(player: Player.Type) : Actor(player) {

            /**
             * Shuffle the deck, preserving it's previous order for undo-ability
             */
            class Shuffle(player: Player.Type) : Deck(player) {

                /**
                 * An ordered list of card id's that represents the order of the deck before this shuffle action
                 * takes place.
                 */
                var previousOrder = emptyList<String>()
                    private set


                override fun apply(board: Board, actor: Player): Player {
                    previousOrder = actor.deck.map { it.id }
                    val shuffledDeck = actor.deck.shuffle(2)
                    return actor.copy(deck = shuffledDeck)
                }
            }


            /**
             * Shuffles the user's hand back into the deck
             */
            class ShuffleHand(player: Player.Type) : Deck(player) {

                /**
                 * An ordered list of card id's that represents the order of the deck before this shuffle action
                 * takes place.
                 */
                var previousOrder = emptyList<String>()
                    private set

                /**
                 * An ordered list of card ids that represents the order of the hand before this shuffle action
                 * takes place.
                 */
                var previousHand = emptyList<String>()
                    private set


                override fun apply(board: Board, actor: Player): Player {
                    previousOrder = actor.deck.map { it.id }
                    previousHand = actor.hand.map { it.id }
                    val shuffledDeck = actor.deck.plus(actor.hand).shuffled()
                    return actor.copy(hand = emptyList(), deck = ArrayDeque(shuffledDeck))
                }
            }


            /**
             * Draw a # of cards from either the top or bottom of the deck
             *
             * @param fromTop whether or not to draw from the top or bottom of the deck
             */
            class Draw(player: Player.Type, val fromTop: Boolean = true) : Deck(player) {

                /**
                 * The number of cards to draw from the deck
                 */
                var count = 1


                override fun apply(board: Board, actor: Player): Player {
                    if (actor.deck.size <= count) {
                        throw PlaytestException.DeckOutException()
                    } else {
                        val deck = actor.deck
                        val draw = if (fromTop) {
                            (0 until count).map { deck.pollFirst() }
                        } else {
                            (0 until count).map { deck.pollLast() }
                        }
                        val hand = actor.hand.plus(draw)
                        return actor.copy(deck = deck, hand = hand)
                    }
                }
            }


            /**
             * Deck Subgroup for search based actions. This includes drawing the search based selections
             * or stacking them at the top/bottom of the deck
             */
            sealed class Search(player: Player.Type, val selection: List<Int>) : Deck(player) {

                /**
                 * Draw the selected cards into the players hand
                 */
                class Draw(player: Player.Type, selection: List<Int>) : Search(player, selection) {

                    override fun apply(board: Board, actor: Player): Player {
                        val deck = actor.deck.toMutableList()
                        val cardsToDraw = selection.map { deck[it] }
                        deck.removeAll(cardsToDraw)
                        val hand = actor.hand.plus(cardsToDraw)
                        return actor.copy(deck = ArrayDeque(deck), hand = hand)
                    }
                }


                /**
                 * Stack the selected cards on the top or bottom of the deck
                 * @param onTop whether or not to stack the cards on the top of the deck, false for stacking on the bottom (top: 1, 2, 3,...,deck; bottom: deck,...,3, 2, 1)
                 */
                class Stack(player: Player.Type, selection: List<Int>, val onTop: Boolean = true) : Search(player, selection) {

                    override fun apply(board: Board, actor: Player): Player {
                        val deck = actor.deck.toMutableList()
                        val cardsToDraw = selection.map { deck[it] }
                        deck.removeAll(cardsToDraw)
                        deck.shuffle()

                        // Stack cards
                        if (onTop) {
                            deck.addAll(0, cardsToDraw)
                        } else {
                            deck.addAll(cardsToDraw.asReversed())
                        }

                        return actor.copy(deck = ArrayDeque(deck))
                    }
                }
            }
        }


        /**
         * Action Group for Active-Card based actions
         */
        sealed class Active(player: Player.Type) : Actor(player) {

            /**
             * Apply a condition to the [player]s active card. Apply [burned] or [poisoned] condition.
             * @param burned whether or not the active pokemon should become burned
             * @param poisoned whether or not the active pokemon should become poisoned
             */
            class Condition(player: Player.Type, val burned: Boolean?, val poisoned: Boolean?) : Active(player) {

                override fun apply(board: Board, actor: Player): Player {
                    val active = actor.active
                    return active?.let {
                        val isBurned = burned ?: it.isBurned
                        val isPoisoned = poisoned ?: it.isPoisoned
                        actor.copy(active = it.copy(isBurned = isBurned, isPoisoned = isPoisoned))
                    } ?: actor
                }
            }


            /**
             * Apply a status (condition) to the [player]s active card.
             * @param status the status (condition) to apply
             * @see Board.Card.Status
             */
            class Status(player: Player.Type, val status: Board.Card.Status) : Active(player) {

                override fun apply(board: Board, actor: Player): Player {
                    val active = actor.active
                    return active?.let {
                        actor.copy(active = it.copy(statusEffect = status))
                    } ?: actor
                }
            }


            /**
             * Swap the active card with the specified [benchPosition]
             * @param benchPosition the position on the bench to swap the active with
             */
            class Swap(player: Player.Type, val benchPosition: Int) : Active(player) {

                override fun apply(board: Board, actor: Player): Player {
                    val active = actor.active
                    return active?.let {
                        val bench = actor.bench.cards[benchPosition]
                        if (bench != null) {
                            val newBench = actor.bench.cards.toMutableMap()
                            newBench[benchPosition] = it
                            actor.copy(active = bench, bench = actor.bench.copy(cards = newBench))
                        } else {
                            actor
                        }
                    } ?: actor
                }
            }
        }


        /**
         * Action Group for Prize based actions
         */
        sealed class Prizes(player: Player.Type) : Actor(player) {

            /**
             * Draw a selected indices of Prize cards into the [target] list on the player
             */
            class Draw(player: Player.Type,
                       val target: Source<List<PokemonCard>, Source.ListBuilder>,
                       val indices: List<Int>) : Prizes(player) {

                override fun apply(board: Board, actor: Player): Player {
                    val prizes = actor.prizes.toMutableMap()
                    val selectedPrizes = indices.map { prizes.remove(it) }.filter { it == null }.map { it!! }
                    val updatedActor = actor.copy(prizes = prizes)
                    return target.apply(updatedActor) {
                        items.addAll(selectedPrizes)
                    }
                }
            }


            /**
             * Add a list of cards from [source] to the [player]'s prize cards
             */
            class Add(player: Player.Type,
                      val source: Source<List<PokemonCard>, Source.ListBuilder>,
                      val cards: List<PokemonCard>) : Actor(player) {

                override fun apply(board: Board, actor: Player): Player {
                    val prizes = actor.prizes.toMutableMap()
                    val lastIndex = prizes.keys.max()?.plus(1) ?: 6
                    cards.forEachIndexed { index, pokemonCard ->
                        prizes[lastIndex + index] = pokemonCard
                    }
                    val updatedActor = actor.copy(prizes = prizes)
                    return source.apply(updatedActor) {
                        items.removeAll(cards)
                    }
                }
            }
        }
    }
}