package com.r0adkll.deckbuilder.arch.ui.features.testing.di


import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingActivity
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingRenderer
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides


@Module
class DeckTestingModule(val activity: DeckTestingActivity) {

    @Provides @ActivityScope
    fun provideUi(): DeckTestingUi = activity


    @Provides @ActivityScope
    fun provideIntentions(): DeckTestingUi.Intentions = activity


    @Provides @ActivityScope
    fun provideActions(): DeckTestingUi.Actions = activity


    @Provides @ActivityScope
    fun provideRenderer(
            actions: DeckTestingUi.Actions,
            schedulers: AppSchedulers
    ) : DeckTestingRenderer = DeckTestingRenderer(actions, schedulers.main, schedulers.comp)
}
