package com.r0adkll.deckbuilder.util.extensions

import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class CardExtensionsTest {

    @Test
    fun `test sortable number with plain number`() {
        val result = createPokemonCard().copy(number = "42").sortableNumber
        result shouldEqualTo 42
    }

    @Test
    fun `test sortable number with special collection prefix`() {
        val result = createPokemonCard().copy(number = "sm57").sortableNumber
        result shouldEqualTo 57
    }

    @Test
    fun `test sortable number between random non-numbers`() {
        val result = createPokemonCard().copy(number = "asdf;-`105abd&#$").sortableNumber
        result shouldEqualTo 105
    }
}
