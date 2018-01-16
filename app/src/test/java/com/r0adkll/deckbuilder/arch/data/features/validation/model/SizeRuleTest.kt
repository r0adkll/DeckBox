package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.tools.ModelUtils
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test


class SizeRuleTest {

    @Test
    fun shouldPassSizeRule() {
        val cards = (0..20).map { ModelUtils.createPokemonCard() }
        val rule = SizeRule()

        val result = rule.check(cards)

        result.shouldBeNull()
    }


    @Test
    fun shouldFailSizeRule() {
        val cards = (0..100).map { ModelUtils.createPokemonCard() }
        val rule = SizeRule()

        val result = rule.check(cards)

        result.shouldNotBeNull()
    }

}