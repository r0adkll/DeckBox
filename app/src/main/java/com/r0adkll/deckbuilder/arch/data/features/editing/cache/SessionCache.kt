package com.r0adkll.deckbuilder.arch.data.features.editing.cache


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import io.reactivex.Observable


interface SessionCache {

    fun createSession(deck: Deck? = null): Observable<Long>
    fun getSession(sessionId: Long): Observable<Session>
    fun deleteSession(sessionId: Long): Observable<Int>
    fun observeSessionCards(sessionId: Long): Observable<List<PokemonCard>>
    fun changeName(sessionId: Long, name: String): Observable<String>
    fun changeDescription(sessionId: Long, description: String): Observable<String>
    fun addCards(sessionId: Long, cards: List<PokemonCard>): Observable<Unit>
    fun removeCard(sessionId: Long, card: PokemonCard): Observable<Unit>
}