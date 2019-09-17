package com.r0adkll.deckbuilder.arch.ui.features.filter.di


import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterFragment
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterRenderer
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi
import com.r0adkll.deckbuilder.internal.di.scopes.FilterScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides


@Module
class FilterModule(val fragment: FilterFragment) {

    @Provides @FilterScope
    fun provideUi(): FilterUi = fragment


    @Provides @FilterScope
    fun provideIntentions(): FilterUi.Intentions = fragment


    @Provides @FilterScope
    fun provideActions(): FilterUi.Actions = fragment


    @Provides @FilterScope
    fun provideRenderer(
            actions: FilterUi.Actions,
            schedulers: AppSchedulers
    ) : FilterRenderer = FilterRenderer(actions, schedulers.main, schedulers.comp)
}
