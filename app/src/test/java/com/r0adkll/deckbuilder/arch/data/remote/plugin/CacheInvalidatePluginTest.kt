package com.r0adkll.deckbuilder.arch.data.remote.plugin

import com.nhaarman.mockitokotlin2.verify
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionVersion
import com.r0adkll.deckbuilder.tools.ModelUtils.EXPANSIONS
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import org.amshove.kluent.*
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class CacheInvalidatePluginTest {

    lateinit var preferences: AppPreferences
    lateinit var expansionDataSource: ExpansionDataSource
    lateinit var remote: Remote
    lateinit var schedulers: Schedulers

    @Before
    fun setUp() {
        preferences = mock()
        expansionDataSource = mock()
        remote = mock()
        schedulers = Schedulers.createTrampoline(false)
    }


    @Test
    fun testInvalidateByVersionCode() {
        When calling remote.expansionVersion itReturns ExpansionVersion(2, "sm8")
        When calling expansionDataSource.getExpansions(any()) itReturns Observable.just(EXPANSIONS.toList())
        When calling expansionDataSource.refreshExpansions() itReturns Observable.just(EXPANSIONS.toList())
        When calling preferences.expansionsVersion itReturns 1
        val plugin = CacheInvalidatePlugin(expansionDataSource, preferences, schedulers)

        plugin.onFetchActivated(remote)

        Verify on expansionDataSource that expansionDataSource.refreshExpansions() was called
        verify(preferences).expansionsVersion = 2
    }


    @Test
    fun testInvalidateByMissingExpansion() {
        When calling remote.expansionVersion itReturns ExpansionVersion(1, "sm8")
        When calling expansionDataSource.getExpansions(any()) itReturns Observable.just(EXPANSIONS.filter { it.code != "sm8" })
        When calling expansionDataSource.refreshExpansions() itReturns Observable.just(EXPANSIONS.toList())
        When calling preferences.expansionsVersion itReturns 1
        val plugin = CacheInvalidatePlugin(expansionDataSource, preferences, schedulers)

        plugin.onFetchActivated(remote)

        Verify on expansionDataSource that expansionDataSource.refreshExpansions() was called
        verify(preferences).expansionsVersion = 1
    }
}