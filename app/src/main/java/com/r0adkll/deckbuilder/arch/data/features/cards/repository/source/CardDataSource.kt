package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.SuperType
import io.reactivex.Observable

interface CardDataSource {

    fun findByExpansion(setCode: String): Observable<List<PokemonCard>>
    fun search(type: SuperType?, query: String, filter: Filter? = null): Observable<List<PokemonCard>>
    fun find(ids: List<String>): Observable<List<PokemonCard>>
}
