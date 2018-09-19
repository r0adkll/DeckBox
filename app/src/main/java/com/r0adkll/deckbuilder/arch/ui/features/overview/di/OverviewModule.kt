package com.r0adkll.deckbuilder.arch.ui.features.overview.di

import com.r0adkll.deckbuilder.arch.ui.features.overview.OverviewFragment
import com.r0adkll.deckbuilder.arch.ui.features.overview.OverviewRenderer
import com.r0adkll.deckbuilder.arch.ui.features.overview.OverviewUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class OverviewModule(val fragment: OverviewFragment) {

    @Provides @FragmentScope
    fun provideUi(): OverviewUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): OverviewUi.Intentions = fragment


    @Provides @FragmentScope
    fun provideActions(): OverviewUi.Actions = fragment


    @Provides @FragmentScope
    fun provideRenderer(
            actions: OverviewUi.Actions,
            schedulers: Schedulers
    ) : OverviewRenderer = OverviewRenderer(actions, schedulers.main, schedulers.comp)
}