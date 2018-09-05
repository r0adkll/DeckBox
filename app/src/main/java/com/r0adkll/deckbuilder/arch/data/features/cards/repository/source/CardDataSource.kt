package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.reactivex.Observable


interface CardDataSource {

    fun getExpansions(): Observable<List<Expansion>>
    fun refreshExpansions(): Observable<List<Expansion>>
}