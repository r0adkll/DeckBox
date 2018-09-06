package com.r0adkll.deckbuilder.arch.data.features.expansions.cache


import com.r0adkll.deckbuilder.arch.data.Remote
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.reactivex.Observable


/**
 * In Memory implementation of [Expansion] cache to store a list of
 * expansions loaded from API in memory so we don't have to keep requesting it
 */
class InMemoryExpansionCache(
        val remote: Remote
) : ExpansionCache {

    private val expansions: ArrayList<Expansion> = ArrayList()


    override fun putExpansions(expansions: List<Expansion>) {
        synchronized(this) {
            this.expansions.clear()
            this.expansions.addAll(expansions)
        }
    }


    override fun getExpansions(): Observable<List<Expansion>> {
        return synchronized(this) {
            if (expansions.none { it.code == remote.expansionVersion?.expansionCode }) {
                expansions.clear()
            }

            Observable.just(expansions.toList())
        }
    }


    override fun clear() {
        synchronized(this) {
            expansions.clear()
        }
    }
}