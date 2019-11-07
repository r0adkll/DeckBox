package com.r0adkll.deckbuilder.arch.data.features.expansions.repository

import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import io.reactivex.Observable

class DefaultExpansionRepository(
    val defaultSource: ExpansionDataSource
) : ExpansionRepository {

    override fun getExpansion(code: String): Observable<Expansion> {
        return defaultSource.getExpansions()
            .map { expansions ->
                expansions.find { it.code == code }
                    ?: throw IllegalArgumentException("No expansion found for $code")
            }
    }

    override fun getExpansions(): Observable<List<Expansion>> {
        return defaultSource.getExpansions()
    }

    override fun refreshExpansions(): Observable<List<Expansion>> {
        return defaultSource.refreshExpansions()
    }
}
