package com.r0adkll.deckbuilder.util.extensions

import com.r0adkll.deckbuilder.arch.data.mappings.CardMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.pokemontcg.model.Card
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

@Suppress("UNCHECKED_CAST")
operator fun Observable<List<Expansion>>.plus(cardSource: Observable<List<Card>>): Observable<List<PokemonCard>> {
    return Observable.combineLatestDelayError(listOf(this, cardSource)) { t: Array<out Any> ->
        val expansions = t[0] as List<Expansion>
        val cards = t[1] as List<Card>
        cards.map { CardMapper.to(it, expansions) }
    }.onErrorReturnItem(emptyList())
}

infix fun <T> Observable<List<T>>.combineLatest(other: Observable<List<T>>): Observable<List<T>> {
    return Observable.combineLatest(this, other, BiFunction { first, second -> first + second })
}
