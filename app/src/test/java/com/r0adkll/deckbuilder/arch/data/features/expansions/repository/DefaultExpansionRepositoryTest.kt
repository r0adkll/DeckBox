package com.r0adkll.deckbuilder.arch.data.features.expansions.repository

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.PreviewExpansionVersion
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
    val previewExpansion = Expansion("sm12", null, "", "", 0, true, true, "", "", null)

    lateinit var defaultSource: ExpansionDataSource
    lateinit var previewSource: ExpansionDataSource
    lateinit var preferences: AppPreferences
    lateinit var remote: Remote
    lateinit var repository: DefaultExpansionRepository

    @Before
    fun setUp() {
        defaultSource = mock()
        previewSource = mock()
        preferences = mock()
        remote = mock()
        repository = DefaultExpansionRepository(defaultSource, previewSource, preferences, remote)

        When calling defaultSource.getExpansions(any()) itReturns Observable.just(listOf(defaultExpansion))
        When calling defaultSource.refreshExpansions() itReturns Observable.just(listOf(
            defaultExpansion,
            refreshedExpansion
        ))
        When calling previewSource.getExpansions(any()) itReturns Observable.just(listOf(previewExpansion))
    }

    @Test
    fun `expansions with remote undefined`() {
        When calling remote.previewExpansionVersion itReturns null

        val result = repository.getExpansions().blockingFirst()

        result.size shouldEqualTo 1
        result shouldContain defaultExpansion
        result shouldNotContain previewExpansion
    }

    @Test
    fun `expansions with preview`() {
        When calling remote.previewExpansionVersion itReturns PreviewExpansionVersion(1, "sm12")

        val result = repository.getExpansions().blockingFirst()

        result.size shouldEqualTo 2
        result shouldContain defaultExpansion
        result shouldContain previewExpansion
    }

    @Test
    fun `refreshed expansions with remote undefined`() {
        When calling remote.previewExpansionVersion itReturns null

        val result = repository.refreshExpansions().blockingFirst()

        result.size shouldEqualTo 2
        result shouldContain defaultExpansion
        result shouldContain refreshedExpansion
        result shouldNotContain previewExpansion
    }

    @Test
    fun `refreshed expansions with remote defined`() {
        When calling remote.previewExpansionVersion itReturns PreviewExpansionVersion(1, "sm12")

        val result = repository.refreshExpansions().blockingFirst()

        result.size shouldEqualTo 3
        result shouldContain defaultExpansion
        result shouldContain refreshedExpansion
        result shouldContain previewExpansion
    }
}
