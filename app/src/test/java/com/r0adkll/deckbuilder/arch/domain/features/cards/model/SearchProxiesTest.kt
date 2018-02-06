package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import com.google.gson.Gson
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

        lowercaseResult.shouldEqualTo("\"N\"")
        uppercaseResult.shouldEqualTo("\"N\"")
    }


    @Test
    fun shouldFixElectricEnergyQuery() {
        val input = "Electric Energy"
        val result = proxies.apply(input)
        result.shouldEqualTo("Lightning Energy")
    }


    @Test
    fun shouldNotFixElectricEnergyQuery() {
        val input = "Electric Memory"
        val result = proxies.apply(input)
        result.shouldEqualTo(input)
    }


    @Test
    fun shouldFixDarkEnergyQuery() {
        val input = "Dark Energy"
        val result = proxies.apply(input)
        result.shouldEqualTo("Darkness Energy")
    }


    @Test
    fun shouldNotFixDarkEnergyQuery() {
        val input = "Dark Memory"
        val result = proxies.apply(input)
        result.shouldEqualTo(input)
    }


    @Test
    fun shouldFixSteelEnergyQuery() {
        val input = "Steel Energy"
        val result = proxies.apply(input)
        result.shouldEqualTo("Metal Energy")
    }


    @Test
    fun shouldNotFixSteelEnergyQuery() {
        val input = "Steel Memory"
        val result = proxies.apply(input)
        result.shouldEqualTo(input)
    }


    @Test
    fun shouldFixEXQuery() {
        val input = "Espeon EX"
        val input2 = "Alolan Execuggtor EX"

        val result = proxies.apply(input)
        val result2 = proxies.apply(input2)

        result.shouldEqualTo("Espeon-EX")
        result2.shouldEqualTo("Alolan Execuggtor-EX")
    }


    @Test
    fun shouldFixGXQuery() {
        val input = "Espeon GX"
        val input2 = "Alolan Execuggtor GX"

        val result = proxies.apply(input)
        val result2 = proxies.apply(input2)

        result.shouldEqualTo("Espeon-GX")
        result2.shouldEqualTo("Alolan Execuggtor-GX")
    }


    @Test
    fun shouldFixMegaQuery() {
        val input = "Mega Mewtwo-GX"
        val input2 = "Mega"

        val result = proxies.apply(input)
        val result2 = proxies.apply(input2)

        result.shouldEqualTo("M Mewtwo-GX")
        result2.shouldEqualTo("Mega")
    }
}