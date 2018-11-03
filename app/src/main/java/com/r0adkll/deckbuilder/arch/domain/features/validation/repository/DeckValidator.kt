package com.r0adkll.deckbuilder.arch.domain.features.validation.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import io.reactivex.Observable


interface DeckValidator {

    fun validate(sessionId: Long): Observable<Validation>
    fun validate(deckId: String): Observable<Validation>
    fun validate(cards: List<PokemonCard>): Observable<Validation>
}