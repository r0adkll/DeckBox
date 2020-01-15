package com.r0adkll.deckbuilder.arch.data.features.testing

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.domain.features.testing.TestResults
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.util.extensions.isMulligan
import com.r0adkll.deckbuilder.util.extensions.shuffle
import io.reactivex.Observable
import javax.inject.Inject

class DefaultDeckTester @Inject constructor(
    val deckRepository: DeckRepository,
    val editRepository: EditRepository,
    val validator: DeckValidator
) : DeckTester {

    override fun testDeck(deck: Deck, iterations: Int): Observable<TestResults> {
        return validator.validate(deck.cards)
            .map {
                if (it.isValid) {
                    test(deck.cards, iterations)
                } else {
                    throw InvalidDeckException()
                }
            }
    }

    override fun testDeckById(deckId: String, iterations: Int): Observable<TestResults> {
        return deckRepository.getDeck(deckId)
            .flatMap { deck ->
                validator.validate(deck.cards)
                    .map {
                        if (it.isValid) {
                            test(deck.cards, iterations)
                        } else {
                            throw InvalidDeckException()
                        }
                    }
            }
    }

    override fun testHandById(deckId: String, iterations: Int): Observable<List<PokemonCard>> {
        return deckRepository.getDeck(deckId)
            .flatMap { deck ->
                validator.validate(deck.cards)
                    .map {
                        if (it.isValid) {
                            deal(deck.cards, iterations)
                        } else {
                            throw InvalidDeckException()
                        }
                    }
            }
    }

    private fun deal(cards: List<PokemonCard>, iterations: Int): List<PokemonCard> {
        val deck = cards.toMutableList()
        (0 until iterations).forEach { _ ->
            deck.shuffle()
        }

        return deck.subList(0, DEFAULT_HAND_SIZE)
    }

    private fun test(cards: List<PokemonCard>, iterations: Int): TestResults {
        var mulligans = 0
        val startingHands = HashMap<PokemonCard, Int>()

        (0..iterations).forEach { _ ->
            val shuffledCards = cards.shuffle(DEFAULT_SHUFFLE_PER_HAND)
            val firstHand = shuffledCards.subList(0, DEFAULT_HAND_SIZE)

            // 1) Check for mulligans
            val didMulligan = firstHand.isMulligan()

            // 2) If we didn't mulligan, then update mapping
            if (didMulligan) {
                mulligans++
            } else {
                firstHand.forEach {
                    val count = startingHands[it] ?: 0
                    startingHands[it] = count + 1
                }
            }
        }

        return TestResults(iterations, mulligans, startingHands)
    }

    companion object {
        const val DEFAULT_HAND_SIZE = 7

        // This value is to emulate the ideal # of riffle shuffles one should do when shuffling the deck
        const val DEFAULT_SHUFFLE_PER_HAND = 7
    }
}
