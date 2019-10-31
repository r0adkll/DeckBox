package com.r0adkll.deckbuilder.arch.ui.features.filter.di

import com.r0adkll.deckbuilder.arch.ui.features.search.DrawerInteractor
import dagger.Module
import dagger.Provides

@Module
class FilterableModule(
        val filterIntentions: FilterIntentions,
        val drawerInteractor: DrawerInteractor
) {

    @Provides
    fun provideFilterIntentions(): FilterIntentions = filterIntentions

    @Provides
    fun provideDrawerInteractor(): DrawerInteractor = drawerInteractor
}
