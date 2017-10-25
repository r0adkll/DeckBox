package com.r0adkll.deckbuilder.arch.ui.features.search.filter.di


import io.pokemontcg.model.SuperType
import io.reactivex.Observable


interface CategoryIntentions {

    fun categoryChange(): Observable<SuperType>
}