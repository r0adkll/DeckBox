package com.r0adkll.deckbuilder.arch.domain.features.expansions.repository

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.reactivex.Observable

interface ExpansionRepository {

    fun getExpansion(code: String): Observable<Expansion>
    fun getExpansions(): Observable<List<Expansion>>
    fun refreshExpansions(): Observable<List<Expansion>>
}
