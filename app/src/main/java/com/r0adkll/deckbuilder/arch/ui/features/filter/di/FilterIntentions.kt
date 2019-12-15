package com.r0adkll.deckbuilder.arch.ui.features.filter.di

import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter

interface FilterIntentions {

    fun filterChanges(): Relay<Filter>
}
