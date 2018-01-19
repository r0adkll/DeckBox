package com.r0adkll.deckbuilder.arch.data.features.validation.model


import com.r0adkll.deckbuilder.tools.ModelUtils.createEnergyCard
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test


class DuplicateRuleTest {

    @Test
    fun shouldNotDetectDuplicates() {
        val cards = listOf(
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Wartortle"),
                createPokemonCard("Wartortle"),
                createPokemonCard("Wartortle"),
                createPokemonCard("Blastoise"),
                createPokemonCard("Blastoise"),
                createPokemonCard("Blastoise"),
                createPokemonCard("Blastoise")
        )
        val rule = DuplicateRule()

        val result = rule.check(cards)

        result.shouldBeNull()
    }


    @Test
    fun shouldDetectDuplicateError() {
        val cards = listOf(
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Wartortle"),
                createPokemonCard("Wartortle"),
                createPokemonCard("Blastoise"),
                createPokemonCard("Blastoise"),
                createPokemonCard("Blastoise"),
                createPokemonCard("Blastoise")
        )
        val rule = DuplicateRule()

        val result = rule.check(cards)

        result.shouldNotBeNull()
    }


    @Test
    fun shouldNotDetectBasicEnergy() {
        val cards = listOf(
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createPokemonCard("Squirtle"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy"),
                createEnergyCard("Water Energy")
        )
        val rule = DuplicateRule()

        val result = rule.check(cards)

        result.shouldBeNull()
    }
}