package com.r0adkll.deckbuilder.arch.data.features.importer.parser

import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBeNull
import org.junit.Test

class LineValidatorTest {

    private val validator = LineValidator()

    @Test
    fun testValidPtcgoLine() {
        val result = validator.validate(PTCGO_LINE)
        result.shouldNotBeNull()
        result.shouldEqual(PLAIN_LINE)
    }

    @Test
    fun testValidPlainLine() {
        val result = validator.validate(PLAIN_LINE)
        result.shouldNotBeNull()
        result.shouldEqual(PLAIN_LINE)
    }

    @Test
    fun testValidJunkLine() {
        val result = validator.validate(JUNK_LINE)
        result.shouldNotBeNull()
        result.shouldEqual(PLAIN_LINE)
    }

    @Test
    fun testValidEnergyLine() {
        val result = validator.validate(ENERGY_LINE)
        result.shouldNotBeNull()
        result.shouldEqual(ENERGY_LINE)
    }

    @Test
    fun testInvalidLineCount() {
        val result = validator.validate(INVALID_LINE_COUNT)
        result.shouldBeNull()
    }

    @Test
    fun testInvalidLineCardCount() {
        val result = validator.validate(INVALID_LINE_CARDCOUNT)
        result.shouldBeNull()
    }

    companion object {
        private const val PTCGO_LINE = "* 4 Articuno-GX CES 31"
        private const val PLAIN_LINE = "4 Articuno-GX CES 31"
        private const val JUNK_LINE = "Somewhere over the rainbow 4 Articuno-GX CES 31"
        private const val ENERGY_LINE = "8 Psychic Energy 5"
        private const val INVALID_LINE_COUNT = "4 Articuno-GX CES"
        private const val INVALID_LINE_CARDCOUNT = "THREE Articuno-GX CES 31"
    }
}
