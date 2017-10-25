package com.r0adkll.deckbuilder.arch.ui.features.search.filter.di


import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterFragment
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterRenderer
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi
import com.r0adkll.deckbuilder.internal.di.FragmentScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class FilterModule(val fragment: FilterFragment) {

    @Provides @FragmentScope
    fun provideUi(): FilterUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): FilterUi.Intentions = fragment


    @Provides @FragmentScope
    fun provideActions(): FilterUi.Actions = fragment


    @Provides @FragmentScope
    fun provideRenderer(
            actions: FilterUi.Actions,
            schedulers: Schedulers
    ) : FilterRenderer = FilterRenderer(actions, schedulers.main, schedulers.comp)
}