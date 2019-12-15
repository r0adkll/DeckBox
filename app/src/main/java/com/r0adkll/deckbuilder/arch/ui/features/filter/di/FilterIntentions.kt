package com.r0adkll.deckbuilder.arch.ui.features.filter.di

import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import io.pokemontcg.model.SuperType
import io.reactivex.Observable

interface FilterIntentions {

    fun filterChanges(): Relay<Filter>
}
