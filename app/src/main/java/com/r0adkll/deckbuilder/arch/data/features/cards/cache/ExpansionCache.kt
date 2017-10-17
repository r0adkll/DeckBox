package com.r0adkll.deckbuilder.arch.data.features.cards.cache


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.reactivex.Observable


interface ExpansionCache {

    fun putExpansions(expansions: List<Expansion>)
    fun getExpansions(): Observable<List<Expansion>>
}