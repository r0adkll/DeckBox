package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.nhaarman.mockito_kotlin.anyOrNull
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.tools.ModelUtils
import com.r0adkll.deckbuilder.tools.ModelUtils.createExpansion
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import com.r0adkll.deckbuilder.tools.mockPreference
import com.r0adkll.deckbuilder.util.helper.Connectivity
import io.reactivex.Observable
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Test
import java.io.IOException


class CombinedSearchDataSourceTest {

    lateinit var diskSource: SearchDataSource
    lateinit var networkSource: SearchDataSource
    lateinit var connectivity: Connectivity
    lateinit var preferences: AppPreferences

    @Before
    fun setUp() {
        diskSource = mock()
        networkSource = mock()
        connectivity = mock()
        preferences = mock()

        When calling networkSource.search(anyOrNull(), any(), anyOrNull()) itReturns Observable.just(emptyList())
        When calling diskSource.search(anyOrNull(), any(), anyOrNull()) itReturns Observable.just(emptyList())
    }

    @Test
    fun testSearchWithConnectivityAndNoCache() {
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = emptySet<String>())
        When calling preferences.offlineExpansions itReturns mockedPreference
        val combinedSearchDataSource = CombinedSearchDataSource(preferences, diskSource, networkSource, connectivity)
        val query = "test"

        combinedSearchDataSource.search(null, query, null)
                .blockingSubscribe()

        Verify on networkSource that networkSource.search(null, query, null) was called
        VerifyNotCalled on diskSource that diskSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithOutConnectivity() {
        When calling connectivity.isConnected() itReturns false
        val mockedPreference = mockPreference(toReturn = emptySet<String>())
        When calling preferences.offlineExpansions itReturns mockedPreference
        val combinedSearchDataSource = CombinedSearchDataSource(preferences, diskSource, networkSource, connectivity)
        val query = "test"

        combinedSearchDataSource.search(null, query, null)
                .blockingSubscribe()

        Verify on diskSource that diskSource.search(null, query, null) was called
        VerifyNotCalled on networkSource that networkSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithConnectivityAndCache() {
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = setOf("sm8"))
        When calling preferences.offlineExpansions itReturns mockedPreference
        val combinedSearchDataSource = CombinedSearchDataSource(preferences, diskSource, networkSource, connectivity)
        val query = "test"
        val filter = Filter()

        combinedSearchDataSource.search(null, query, filter)
                .blockingSubscribe()

        Verify on networkSource that networkSource.search(null, query, filter) was called
        VerifyNotCalled on diskSource that diskSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithConnectivtyAndMatchingCache() {
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = setOf("sm8", "sm7", "sm6", "sm75"))
        When calling preferences.offlineExpansions itReturns mockedPreference
        val combinedSearchDataSource = CombinedSearchDataSource(preferences, diskSource, networkSource, connectivity)
        val query = "test"
        val filter = Filter(expansions = listOf(createExpansion("sm8", "sm6")))

        combinedSearchDataSource.search(null, query, filter)
                .blockingSubscribe()

        Verify on diskSource that diskSource.search(null, query, filter) was called
        VerifyNotCalled on networkSource that networkSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithConnectivityFailureFallback() {
        When calling networkSource.search(anyOrNull(), any(), anyOrNull()) itReturns Observable.error(IOException())
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = emptySet<String>())
        When calling preferences.offlineExpansions itReturns mockedPreference
        val combinedSearchDataSource = CombinedSearchDataSource(preferences, diskSource, networkSource, connectivity)
        val query = "test"

        combinedSearchDataSource.search(null, query, null)
                .blockingSubscribe()

        Verify on diskSource that diskSource.search(null, query, null) was called
    }


    @Test
    fun testFindWithConnectivity() {
        val cards = listOf(
                createPokemonCard().copy(id = "sm8-1"),
                createPokemonCard().copy(id = "sm8-2"),
                createPokemonCard().copy(id = "sm8-3"),
                createPokemonCard().copy(id = "sm8-4")
        )
        val ids = cards.map { it.id }
        When calling connectivity.isConnected() itReturns true
        When calling diskSource.find(any()) itReturns Observable.just(cards)
        val combinedSearchDataSource = CombinedSearchDataSource(preferences, diskSource, networkSource, connectivity)

        val results = combinedSearchDataSource.find(ids)
                .blockingFirst()

        results shouldEqual cards
        VerifyNotCalled on networkSource that networkSource.find(any())
    }
}
