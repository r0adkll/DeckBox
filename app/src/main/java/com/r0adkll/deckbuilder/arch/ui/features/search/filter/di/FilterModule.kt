package com.r0adkll.deckbuilder.arch.ui.features.search.filter.di


import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterFragment
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi
import com.r0adkll.deckbuilder.internal.di.FragmentScope
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
}