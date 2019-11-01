package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di

import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchFragment
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchRenderer
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class UnifiedSearchModule(val fragment: SearchFragment) {

    @Provides
    @FragmentScope
    fun provideUi(): SearchUi = fragment

    @Provides
    @FragmentScope
    fun provideIntentions(): SearchUi.Intentions = fragment

    @Provides
    @FragmentScope
    fun provideActions(): SearchUi.Actions = fragment

    @Provides
    @FragmentScope
    fun provideRenderer(
        actions: SearchUi.Actions,
        schedulers: AppSchedulers
    ): SearchRenderer = SearchRenderer(actions, schedulers.main, schedulers.comp)
}
