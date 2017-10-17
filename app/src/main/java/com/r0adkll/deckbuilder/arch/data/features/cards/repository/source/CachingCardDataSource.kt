package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source


import com.r0adkll.deckbuilder.arch.data.features.cards.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.mappings.SetMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.pokemontcg.Pokemon
import io.reactivex.Observable
import javax.inject.Inject


class CachingCardDataSource @Inject constructor(
        val api: Pokemon,
        val expansionCache: ExpansionCache
) : CardDataSource {

    override fun getExpansions(): Observable<List<Expansion>> {
        return Observable.concat(cache(), network())
                .takeUntil { it.isNotEmpty() }
                .filter { it.isNotEmpty() }
    }


    private fun network(): Observable<List<Expansion>> {
        return api.set()
                .observeAll()
                .map { it.map { SetMapper.to(it) } }
                .doOnNext { expansionCache.putExpansions(it) }
    }


    private fun cache(): Observable<List<Expansion>> {
        return expansionCache.getExpansions()
    }
}