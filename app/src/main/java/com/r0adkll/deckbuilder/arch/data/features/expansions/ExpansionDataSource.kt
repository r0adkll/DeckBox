package com.r0adkll.deckbuilder.arch.data.features.expansions


import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.reactivex.Observable


interface ExpansionDataSource {

    fun getExpansions(source: Source = Source.ALL): Observable<List<Expansion>>
    fun refreshExpansions(): Observable<List<Expansion>>
    fun clearExpansions()
}