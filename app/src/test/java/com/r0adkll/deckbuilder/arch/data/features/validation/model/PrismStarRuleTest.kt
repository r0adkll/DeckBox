package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test

class PrismStarRuleTest {

    @Test
    fun shouldNotDetectDuplicates() {
        val cards = listOf(
            createPokemonCard("Lunala ◇"),
            createPokemonCard("Solgaleo ◇"),
            createPokemonCard("Giratina ◇"),
            createPokemonCard("Darkrai ◇"),
            createPokemonCard("Super Boost Energy ◇"),
            createPokemonCard("Cyrus ◇")
        )
        val rule = PrismStarRule()

        val result = rule.check(cards)

        result.shouldBeNull()
    }

    @Test
    fun shouldDetectDuplicateError() {
        val cards = listOf(
            createPokemonCard("Lunala ◇"),
            createPokemonCard("Lunala ◇"),
            createPokemonCard("Solgaleo ◇"),
            createPokemonCard("Giratina ◇"),
            createPokemonCard("Darkrai ◇"),
            createPokemonCard("Super Boost Energy ◇"),
            createPokemonCard("Cyrus ◇")
        )
        val rule = PrismStarRule()

        val result = rule.check(cards)

        result.shouldNotBeNull()
    }
}
