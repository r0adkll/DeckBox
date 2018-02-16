package com.r0adkll.deckbuilder.arch.data.features.editing.cache


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import io.reactivex.Observable


interface SessionCache {

    fun createSession(deck: Deck?, imports: List<PokemonCard>?): Observable<Long>
    fun getSession(sessionId: Long): Observable<Session>
    fun observeSession(sessionId: Long): Observable<Session>
    fun deleteSession(sessionId: Long): Observable<Int>
    fun resetSession(sessionId: Long): Observable<Unit>
    fun changeName(sessionId: Long, name: String): Observable<String>
    fun changeDescription(sessionId: Long, description: String): Observable<String>
    fun addCards(sessionId: Long, cards: List<PokemonCard>, searchSessionId: String? = null): Observable<Unit>
    fun removeCard(sessionId: Long, card: PokemonCard, searchSessionId: String? = null): Observable<Unit>
    fun clearSearchSession(sessionId: Long, searchSessionId: String): Observable<Unit>
}