package com.r0adkll.deckbuilder.arch.data.features.expansions.repository

import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.reactivex.Observable
import org.amshove.kluent.When
import org.amshove.kluent.any
import org.amshove.kluent.calling
import org.amshove.kluent.itReturns
import org.amshove.kluent.mock
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Before
import org.junit.Test

class DefaultExpansionRepositoryTest {

    val defaultExpansion = Expansion("sm11", null, "", "", 0, true, true, "", "", null)
    val refreshedExpansion = Expansion("sm115", null, "", "", 0, true, true, "", "", null)

    lateinit var defaultSource: ExpansionDataSource
    lateinit var repository: DefaultExpansionRepository

    @Before
    fun setUp() {
        defaultSource = mock()
        repository = DefaultExpansionRepository(defaultSource)

        When calling defaultSource.getExpansions(any()) itReturns Observable.just(listOf(defaultExpansion))
        When calling defaultSource.refreshExpansions() itReturns Observable.just(listOf(
            defaultExpansion,
            refreshedExpansion
        ))
    }

    @Test
    fun `get expansions`() {
        val result = repository.getExpansions().blockingFirst()

        result.size shouldEqualTo 1
        result shouldContain defaultExpansion
        result shouldNotContain refreshedExpansion
    }

    @Test
    fun `refreshed expansions`() {
        val result = repository.refreshExpansions().blockingFirst()

        result.size shouldEqualTo 2
        result shouldContain defaultExpansion
        result shouldContain refreshedExpansion
    }
}
