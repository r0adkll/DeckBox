package com.r0adkll.deckbuilder.arch.data.features.validation.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.BanList
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.LegalOverrides
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.Reprints
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.tools.ModelUtils.EXPANSIONS
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import io.reactivex.Observable
import org.amshove.kluent.When
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeNull
import org.junit.Before
import org.junit.Test

class DefaultDeckValidatorTest {

    lateinit var expansionRepository: ExpansionRepository
    lateinit var deckRepository: DeckRepository
    lateinit var editRepository: EditRepository
    lateinit var remote: Remote
    lateinit var validator: DefaultDeckValidator

    @Before
    fun setUp() {
        expansionRepository = mock()
        deckRepository = mock()
        editRepository = mock()
        remote = mock()

        val rules = emptySet<Rule>()
        validator = DefaultDeckValidator(rules, expansionRepository, deckRepository, editRepository, remote)
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
        val reprint = createPokemonCard(name = "Ultra Ball", expansionCode = "xy11")
            .copy(text = listOf("Discard 2 cards and search the deck for any pokemon card"))
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
        val reprint = createPokemonCard(name = "Ultra Ball", expansionCode = "hgss1")
            .copy(text = listOf("Discard 2 cards and search the deck for any pokemon card"))
        val reprints = Reprints(emptySet(), setOf(reprint.reprintHash()))
        When calling remote.reprints itReturns reprints
        val pokemon = (0 until 59).map { createPokemonCard(name = "$it", expansionCode = "xy1") }.plus(reprint)

        val result = validator.validate(pokemon).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeTrue()
        result.rules.shouldBeEmpty()
    }

    @Test
    fun testValidateBanListWithStandard() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charmander", expansionCode = "sm8").copy(id = "sm8-45")
        When calling remote.banList itReturns BanList(listOf("sm8-45"))

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeTrue()
    }

    @Test
    fun testValidateBanListWithExpanded() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charmander", expansionCode = "xy7").copy(id = "xy7-45")
        When calling remote.banList itReturns BanList(emptyList(), listOf("xy7-45"))

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeFalse()
    }

    @Test
    fun testValidateBanListExpandedWithUpperCase() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charmander", expansionCode = "xy7").copy(id = "XY7-45")
        When calling remote.banList itReturns BanList(emptyList(), listOf("xy7-45"))

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeFalse()
    }

    @Test
    fun testValidateLegalPromoOverrideLegal() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charizard", expansionCode = "smp")
            .copy(id = "smp-sm100", number = "SM100")
        val override = LegalOverrides(LegalOverrides.Promo("smp", 94), null, emptyList())
        When calling remote.legalOverrides itReturns override

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeTrue()
        result.expanded.shouldBeTrue()
    }

    @Test
    fun testValidateLegalPromoOverrideLegalFail() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charizard", expansionCode = "smp")
            .copy(id = "smp-sm54", number = "SM54")
        val override = LegalOverrides(LegalOverrides.Promo("smp", 94), null, emptyList())
        When calling remote.legalOverrides itReturns override

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeTrue()
    }

    @Test
    fun testValidateLegalExpandedPromoOverrideLegal() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charizard", expansionCode = "xy9")
            .copy(id = "xy9-sm100", number = "SM100")
        val override = LegalOverrides(null, LegalOverrides.Promo("xy9", 94), emptyList())
        When calling remote.legalOverrides itReturns override

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeTrue()
    }

    @Test
    fun testValidateLegalExpandedPromoOverrideLegalFail() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charizard", expansionCode = "xy9")
            .copy(id = "xy9-sm54", number = "SM54")
        val override = LegalOverrides(null, LegalOverrides.Promo("xy9", 94), emptyList())
        When calling remote.legalOverrides itReturns override

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeFalse()
        result.expanded.shouldBeFalse()
    }

    @Test
    fun testValidateLegalOverrideSingles() {
        setupExpansionMock(EXPANSIONS.toList())
        val pokemon = createPokemonCard(name = "Charizard", expansionCode = "sm115").copy(id = "sm115-SV5")
        val override = LegalOverrides(null, null, listOf(LegalOverrides.Single("sm115-SV5", "sm5-15", "sm5")))
        When calling remote.legalOverrides itReturns override

        val result = validator.validate(listOf(pokemon)).blockingFirst()

        result.shouldNotBeNull()
        result.standard.shouldBeTrue()
        result.expanded.shouldBeTrue()
    }

    private fun setupExpansionMock(items: List<Expansion>) {
        When calling expansionRepository.getExpansions() itReturns Observable.just(items)
    }

    private fun PokemonCard.reprintHash(): Long {
        return (this.name.hashCode().toLong() * 31L) +
            (this.text?.hashCode()?.toLong() ?: 0L * 31L)
    }
}
