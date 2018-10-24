package com.r0adkll.deckbuilder.arch.data.features.cards.cache

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.r0adkll.deckbuilder.arch.data.databasev2.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.databasev2.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.databasev2.relations.CardWithAttacks
import com.r0adkll.deckbuilder.arch.data.databasev2.relations.SessionCard
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.unstack
import io.pokemontcg.model.Card
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject


class RoomCardCache @Inject constructor(
        val db: DeckDatabase,
        val cache: ExpansionDataSource,
        val remote: Remote
) : CardCache {


    override fun putCards(cards: List<Card>) {
        val entities = cards.map { RoomEntityMapper.to(it) }
        db.cards().insertCardsWithAttacks(entities)
    }

    override fun findCards(ids: List<String>): Observable<List<PokemonCard>> {
        return Observable.combineLatest(db.cards().getCards(ids).toObservable(), cache.getExpansions(),
                BiFunction<List<CardWithAttacks>, List<Expansion>, List<PokemonCard>> { cards, expansions ->
                    RoomEntityMapper.fromCards(expansions, cards)
                })
    }

    override fun findCards(query: String, filter: Filter?): Observable<List<PokemonCard>> {


        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}