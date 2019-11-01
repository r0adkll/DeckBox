package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di

import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderRenderer
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class DeckBuilderModule(
    val activity: DeckBuilderActivity
) {

    @Provides
    @ActivityScope
    fun provideUi(): DeckBuilderUi = activity

    @Provides
    @ActivityScope
    fun provideIntentions(): DeckBuilderUi.Intentions = activity

    @Provides
    @ActivityScope
    fun provideActions(): DeckBuilderUi.Actions = activity

    @Provides
    @ActivityScope
    fun provideRenderer(
        actions: DeckBuilderUi.Actions,
        schedulers: AppSchedulers
    ): DeckBuilderRenderer = DeckBuilderRenderer(actions, schedulers.main, schedulers.comp)
}
