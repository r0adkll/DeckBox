package com.r0adkll.deckbuilder.arch.ui.features.browser.di


import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseFragment
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseRenderer
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class BrowseModule(val fragment: BrowseFragment) {

    @Provides @FragmentScope
    fun provideUi(): BrowseUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): BrowseUi.Intentions = fragment


    @Provides @FragmentScope
    fun provideActions(): BrowseUi.Actions = fragment


    @Provides @FragmentScope
    fun provideRenderer(
            actions: BrowseUi.Actions,
            schedulers: Schedulers
    ) : BrowseRenderer = BrowseRenderer(actions, schedulers.main, schedulers.comp)
}