package com.r0adkll.deckbuilder.arch.data.remote.plugin

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import org.amshove.kluent.mock
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class CacheInvalidatePluginTest {

    lateinit var preferences: AppPreferences
    lateinit var expansionDataSource: ExpansionDataSource

    @Before
    fun setUp() {
        preferences = mock()
        expansionDataSource = mock()
    }


    @Test
    fun testInvalidateByVersionCode() {

    }


    @Test
    fun testInvalidateByMissingExpansion() {

    }
}