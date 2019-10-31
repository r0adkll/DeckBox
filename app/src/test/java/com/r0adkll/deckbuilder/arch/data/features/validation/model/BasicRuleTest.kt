package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import io.pokemontcg.model.SubType.GX
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test

class BasicRuleTest {

    @Test
    fun shouldPassBasicRule() {
        val cards = listOf(
                createPokemonCard(),
                createPokemonCard().copy(subtype = GX),
                createPokemonCard().copy(subtype = GX, evolvesFrom = "Squirtle")
        )
        val rule = BasicRule()

        val result = rule.check(cards)

        result.shouldBeNull()
    }

    @Test
    fun shouldFailBasicRule() {
        val cards = listOf(
                createPokemonCard().copy(evolvesFrom = "Charmander"),
                createPokemonCard().copy(evolvesFrom = "Charizard"),
                createPokemonCard().copy(evolvesFrom = "Piplup")
        )
        val rule = BasicRule()

        val result = rule.check(cards)

        result.shouldNotBeNull()
    }
}
