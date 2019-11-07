package com.r0adkll.deckbuilder.arch.data.features.expansions.cache

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.reactivex.Observable

interface ExpansionCache {

    fun putExpansions(expansions: List<Expansion>)
    fun getExpansions(): Observable<List<Expansion>>
    fun clear()

    /**
     * Details the source from which you wish to grab the expansions
     */
    enum class Source {
        ALL,
        LOCAL,
        NETWORK
    }
}
