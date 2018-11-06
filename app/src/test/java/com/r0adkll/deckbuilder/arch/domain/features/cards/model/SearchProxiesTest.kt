package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import com.google.gson.Gson
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.SearchProxies
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.junit.Before
import org.junit.Test


class SearchProxiesTest {

    val proxyConfig = "{\"proxies\":[{\"regex\":\"\\\\bn\\\\b\",\"replacement\":\"\\\"N\\\"\"}," +
            "{\"regex\":\"\\\\b(electric)\\\\b(?=( +energy))\",\"replacement\":\"Lightning\"}," +
            "{\"regex\":\"\\\\b(dark)\\\\b(?=( +energy))\",\"replacement\":\"Darkness\"}," +
            "{\"regex\":\"\\\\b(steel)\\\\b(?=( +energy))\",\"replacement\":\"Metal\"}," +
            "{\"regex\":\"\\\\b( GX)\\\\b\",\"replacement\":\"-GX\"}," +
            "{\"regex\":\"\\\\b( EX)\\\\b\",\"replacement\":\"-EX\"}," +
            "{\"regex\":\"(Mega)(?= \\\\w)\",\"replacement\":\"M\"}]}"
    val gson = Gson()

    lateinit var proxies: SearchProxies

    @Before
    fun setUp() {
        proxies = gson.fromJson(proxyConfig, SearchProxies::class.java)
    }


    @Test
    fun shouldWrapQuotesAroundNSupporter() {
        val input = "n"
        val inputUpper = "N"

        val lowercaseResult = proxies.apply(input)
        val uppercaseResult = proxies.apply(inputUpper)

        lowercaseResult.shouldBeEqualTo("\"N\"")
        uppercaseResult.shouldBeEqualTo("\"N\"")
    }


    @Test
    fun shouldFixElectricEnergyQuery() {
        val input = "Electric Energy"
        val result = proxies.apply(input)
        result.shouldBeEqualTo("Lightning Energy")
    }


    @Test
    fun shouldNotFixElectricEnergyQuery() {
        val input = "Electric Memory"
        val result = proxies.apply(input)
        result.shouldBeEqualTo(input)
    }


    @Test
    fun shouldFixDarkEnergyQuery() {
        val input = "Dark Energy"
        val result = proxies.apply(input)
        result.shouldBeEqualTo("Darkness Energy")
    }


    @Test
    fun shouldNotFixDarkEnergyQuery() {
        val input = "Dark Memory"
        val result = proxies.apply(input)
        result.shouldBeEqualTo(input)
    }


    @Test
    fun shouldFixSteelEnergyQuery() {
        val input = "Steel Energy"
        val result = proxies.apply(input)
        result.shouldBeEqualTo("Metal Energy")
    }


    @Test
    fun shouldNotFixSteelEnergyQuery() {
        val input = "Steel Memory"
        val result = proxies.apply(input)
        result.shouldBeEqualTo(input)
    }


    @Test
    fun shouldFixEXQuery() {
        val input = "Espeon EX"
        val input2 = "Alolan Execuggtor EX"

        val result = proxies.apply(input)
        val result2 = proxies.apply(input2)

        result.shouldBeEqualTo("Espeon-EX")
        result2.shouldBeEqualTo("Alolan Execuggtor-EX")
    }


    @Test
    fun shouldFixGXQuery() {
        val input = "Espeon GX"
        val input2 = "Alolan Execuggtor GX"

        val result = proxies.apply(input)
        val result2 = proxies.apply(input2)

        result.shouldBeEqualTo("Espeon-GX")
        result2.shouldBeEqualTo("Alolan Execuggtor-GX")
    }


    @Test
    fun shouldFixMegaQuery() {
        val input = "Mega Mewtwo-GX"
        val input2 = "Mega"

        val result = proxies.apply(input)
        val result2 = proxies.apply(input2)

        result.shouldBeEqualTo("M Mewtwo-GX")
        result2.shouldBeEqualTo("Mega")
    }
}