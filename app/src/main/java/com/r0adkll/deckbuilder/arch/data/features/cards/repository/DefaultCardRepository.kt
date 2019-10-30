package com.r0adkll.deckbuilder.arch.data.features.cards.repository

import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CardDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import java.io.IOException
import javax.inject.Inject

class DefaultCardRepository @Inject constructor(
        val cardDataSource: CardDataSource,
        val schedulers: AppSchedulers
) : CardRepository {

    override fun findByExpansion(setCode: String): Observable<List<PokemonCard>> {
        return cardDataSource.findByExpansion(setCode)
    }

    override fun search(type: SuperType?, text: String, filter: Filter?): Observable<List<PokemonCard>> {
        return cardDataSource.search(type, text, filter)
    }

    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return cardDataSource.find(ids)
                .switchIfEmpty(Observable.error(IOException("Unable to find cards")))
    }
}
