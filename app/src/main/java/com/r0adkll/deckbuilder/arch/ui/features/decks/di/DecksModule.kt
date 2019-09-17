package com.r0adkll.deckbuilder.arch.ui.features.decks.di


import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksFragment
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksRenderer
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides


@Module
class DecksModule(val fragment: DecksFragment) {

    @Provides @FragmentScope
    fun provideUi(): DecksUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): DecksUi.Intentions = fragment


    @Provides @FragmentScope
    fun provideActions(): DecksUi.Actions = fragment


    @Provides @FragmentScope
    fun provideRenderer(
            actions: DecksUi.Actions,
            schedulers: AppSchedulers
    ) : DecksRenderer = DecksRenderer(actions, schedulers.main, schedulers.comp)

}
