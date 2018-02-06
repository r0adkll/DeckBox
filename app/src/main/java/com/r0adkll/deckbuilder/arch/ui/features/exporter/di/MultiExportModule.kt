package com.r0adkll.deckbuilder.arch.ui.features.exporter.di


import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides


@Module
class MultiExportModule(val deck: Deck) {

    @Provides @ActivityScope
    fun provideDeck(): Deck = deck

}