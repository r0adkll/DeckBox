package com.r0adkll.deckbuilder.arch.data.features.cards.repository


import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CardDataSource
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.search.SearchDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import java.io.IOException
import javax.inject.Inject


class DefaultCardRepository @Inject constructor(
        val dataSource: CardDataSource,
        val searchDataSource: SearchDataSource
) : CardRepository {

    override fun getExpansions(): Observable<List<Expansion>> {
        return dataSource.getExpansions()
    }


    override fun refreshExpansions(): Observable<List<Expansion>> {
        return dataSource.refreshExpansions()
    }


    override fun search(type: SuperType?, text: String, filter: Filter?): Observable<List<PokemonCard>> {
        return searchDataSource.search(type, text, filter)
    }


    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return searchDataSource.find(ids)
                .switchIfEmpty(Observable.error(IOException("Unable to find cards")))
    }
}