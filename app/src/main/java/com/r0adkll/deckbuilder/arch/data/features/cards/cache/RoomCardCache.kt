package com.r0adkll.deckbuilder.arch.data.features.cards.cache

import com.r0adkll.deckbuilder.arch.data.databasev2.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.databasev2.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.Card
import io.reactivex.Observable
import javax.inject.Inject


class RoomCardCache @Inject constructor(
        val db: DeckDatabase,
        val cache: ExpansionDataSource,
        val remote: Remote
) : CardCache {


    override fun putCards(cards: List<Card>) {
        val entities = cards.map { RoomEntityMapper.to(it) }
    }

    override fun findCards(ids: List<String>): Observable<List<PokemonCard>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findCards(query: String, filter: Filter?): Observable<List<PokemonCard>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}