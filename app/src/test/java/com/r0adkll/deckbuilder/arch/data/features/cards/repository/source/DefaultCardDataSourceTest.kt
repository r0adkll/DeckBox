package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.nhaarman.mockitokotlin2.anyOrNull
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.tools.ModelUtils.createExpansion
import com.r0adkll.deckbuilder.tools.ModelUtils.createPokemonCard
import com.r0adkll.deckbuilder.tools.mockPreference
import com.r0adkll.deckbuilder.util.helper.Connectivity
import io.reactivex.Observable
import org.amshove.kluent.Verify
import org.amshove.kluent.VerifyNotCalled
import org.amshove.kluent.When
import org.amshove.kluent.any
import org.amshove.kluent.called
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.amshove.kluent.on
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.that
import org.amshove.kluent.was
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DefaultCardDataSourceTest {

    lateinit var diskSource: CardDataSource
    lateinit var networkSource: CardDataSource
    lateinit var connectivity: Connectivity
    lateinit var preferences: AppPreferences
    lateinit var remote: Remote
    lateinit var source: DefaultCardDataSource

    @Before
    fun setUp() {
        diskSource = mock()
        networkSource = mock()
        connectivity = mock()
        preferences = mock()
        remote = mock()

        When calling networkSource.search(anyOrNull(), any(), anyOrNull()) itReturns Observable.just(emptyList())
        When calling diskSource.search(anyOrNull(), any(), anyOrNull()) itReturns Observable.just(emptyList())
        source = DefaultCardDataSource(preferences, diskSource, networkSource, connectivity, remote)
    }

    @Test
    fun testSearchWithConnectivityAndNoCache() {
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = emptySet<String>())
        When calling preferences.offlineExpansions itReturns mockedPreference
        val query = "test"

        source.search(null, query, null)
            .blockingSubscribe()

        Verify on networkSource that networkSource.search(null, query, null) was called
        VerifyNotCalled on diskSource that diskSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithOutConnectivity() {
        When calling connectivity.isConnected() itReturns false
        val mockedPreference = mockPreference(toReturn = emptySet<String>())
        When calling preferences.offlineExpansions itReturns mockedPreference
        val query = "test"

        source.search(null, query, null)
            .blockingSubscribe()

        Verify on diskSource that diskSource.search(null, query, null) was called
        VerifyNotCalled on networkSource that networkSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithConnectivityAndCache() {
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = setOf("sm8"))
        When calling preferences.offlineExpansions itReturns mockedPreference
        val query = "test"
        val filter = Filter()

        source.search(null, query, filter)
            .blockingSubscribe()

        Verify on networkSource that networkSource.search(null, query, filter) was called
        VerifyNotCalled on diskSource that diskSource.search(anyOrNull(), any(), anyOrNull())
    }

    @Test
    fun testSearchWithConnectivtyAndMatchingCache() {
        When calling connectivity.isConnected() itReturns true
        val mockedPreference = mockPreference(toReturn = setOf("sm8", "sm7", "sm6", "sm75"))
        When calling preferences.offlineExpansions itReturns mockedPreference
        val query = "test"
        val filter = Filter(expansions = listOf(createExpansion("sm8", "sm6")))

        source.search(null, query, filter)
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
        val query = "test"

        source.search(null, query, null)
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

        val results = source.find(ids).blockingFirst()

        results shouldEqual cards
        VerifyNotCalled on networkSource that networkSource.find(any())
    }

    @Test
    fun testFindIncompleteWithConnectivity() {
        val cards = listOf(
            createPokemonCard().copy(id = "sm8-1"),
            createPokemonCard().copy(id = "sm8-2"),
            createPokemonCard().copy(id = "sm8-3"),
            createPokemonCard().copy(id = "sm8-4")
        )
        val ids = cards.map { it.id }
        When calling connectivity.isConnected() itReturns true
        When calling diskSource.find(any()) itReturns Observable.just(cards.take(3))
        When calling networkSource.find(any()) itReturns Observable.just(cards.subList(3, 4))

        val results = source.find(ids).blockingFirst()

        results shouldContainAll cards
        Verify on diskSource that diskSource.find(ids) was called
        Verify on networkSource that networkSource.find(listOf("sm8-4")) was called
    }

    @Test
    fun testFindWithoutConnectivity() {
        val cards = listOf(
            createPokemonCard().copy(id = "sm8-1"),
            createPokemonCard().copy(id = "sm8-2"),
            createPokemonCard().copy(id = "sm8-3"),
            createPokemonCard().copy(id = "sm8-4")
        )
        val ids = cards.map { it.id }
        When calling connectivity.isConnected() itReturns false
        When calling diskSource.find(any()) itReturns Observable.just(cards)

        val results = source.find(ids).blockingFirst()

        results shouldEqual cards
        Verify on diskSource that diskSource.find(ids) was called
        VerifyNotCalled on networkSource that networkSource.find(any())
    }
}
