package com.r0adkll.deckbuilder.arch.data.features.validation.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.Reprints
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.tools.ModelUtils.EXPANSIONS
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import io.reactivex.Observable
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Test


class DefaultDeckValidatorTest {

    lateinit var cardRepository: CardRepository
    lateinit var deckRepository: DeckRepository
    lateinit var editRepository: EditRepository
    lateinit var remote: Remote
    lateinit var validator: DefaultDeckValidator


    @Before
    fun setUp() {
        cardRepository = mock()
        deckRepository = mock()
        editRepository = mock()
        remote = mock()

        val rules = emptySet<Rule>()
        validator = DefaultDeckValidator(rules, cardRepository, deckRepository, editRepository, remote)
    }


    @Test
    fun testValidateStandard() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = (0 until 60).map { createPokemonCard(name = "$it", expansionCode = "sm7") }

        val result = validator.validate(pokemon).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeTrue()
        result.expanded.shouldBeTrue()
        result.rules.shouldBeEmpty()
    }


    @Test
    fun testValidateExpandedOnly() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = (0 until 60).map { createPokemonCard(name = "$it", expansionCode = "xy11") }

        val result = validator.validate(pokemon).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeTrue()
        result.rules.shouldBeEmpty()
    }


    @Test
    fun testValidateStandardWithReprint() {
        setupExpansionMock(EXPANSIONS.toList())
        val reprint = createPokemonCard(name = "Ultra Ball", expansionCode = "xy11").copy(text = listOf("Discard 2 cards and search the deck for any pokemon card"))
        val reprints = Reprints(setOf(reprint.reprintHash()), emptySet())
        When calling remote.reprints itReturns reprints
        val pokemon = (0 until 59).map { createPokemonCard(name = "$it", expansionCode = "sm7") }.plus(reprint)

        val result = validator.validate(pokemon).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeTrue()
        result.expanded.shouldBeTrue()
        result.rules.shouldBeEmpty()
    }


    @Test
    fun testValidateExpandedWithReprint() {
        setupExpansionMock(EXPANSIONS.toList())
        val reprint = createPokemonCard(name = "Ultra Ball", expansionCode = "hgss1").copy(text = listOf("Discard 2 cards and search the deck for any pokemon card"))
        val reprints = Reprints(emptySet(), setOf(reprint.reprintHash()))
        When calling remote.reprints itReturns reprints
        val pokemon = (0 until 59).map { createPokemonCard(name = "$it", expansionCode = "xy1") }.plus(reprint)

        val result = validator.validate(pokemon).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeTrue()
        result.rules.shouldBeEmpty()
    }


    private fun setupExpansionMock(items: List<Expansion>) {
        When calling cardRepository.getExpansions() itReturns Observable.just(items)
    }


    private fun PokemonCard.reprintHash(): Long {
        return (this.name.hashCode().toLong() * 31L) +
                (this.text?.hashCode()?.toLong() ?: 0L * 31L)
    }
}