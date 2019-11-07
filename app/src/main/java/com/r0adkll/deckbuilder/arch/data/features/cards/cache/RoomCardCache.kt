package com.r0adkll.deckbuilder.arch.data.features.cards.cache

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.database.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.database.relations.CardWithAttacks
import com.r0adkll.deckbuilder.arch.data.database.util.FilterQueryHelper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import io.pokemontcg.model.Card
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class RoomCardCache @Inject constructor(
    val db: DeckDatabase,
    val repository: ExpansionRepository,
    val remote: Remote
) : CardCache {

    override fun putCards(cards: List<Card>) {
        val entities = cards.map { RoomEntityMapper.to(it) }
        db.cards().insertCardsWithAttacks(entities)
    }

    override fun findCards(ids: List<String>): Observable<List<PokemonCard>> {
        return Observable.combineLatest(db.cards().getCardsSplit(ids).toObservable(), repository.getExpansions(),
            BiFunction<List<CardWithAttacks>, List<Expansion>, List<PokemonCard>> { cards, expansions ->
                RoomEntityMapper.fromCards(expansions, cards)
            })
    }

    override fun findCards(query: String, filter: Filter?): Observable<List<PokemonCard>> {
        val adjustedQuery = remote.searchProxies?.apply(query) ?: query
        val search = db.cards().searchCards(FilterQueryHelper.createQuery(adjustedQuery, filter)).toObservable()
        val expansions = repository.getExpansions()

        return Observable.combineLatest(
            search,
            expansions,
            BiFunction<List<CardWithAttacks>, List<Expansion>, List<PokemonCard>> { c, e ->
                RoomEntityMapper.fromCards(e, c)
            }
        )
    }

    override fun clear() {
        db.cards().clear()
    }
}
