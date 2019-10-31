package com.r0adkll.deckbuilder.arch.ui.features.missingcards.di

import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsActivity
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsRenderer
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class MissingCardsModule(val activity: MissingCardsActivity) {

    @Provides @ActivityScope
    fun provideUi(): MissingCardsUi = activity

    @Provides @ActivityScope
    fun provideIntentions(): MissingCardsUi.Intentions = activity

    @Provides @ActivityScope
    fun provideActions(): MissingCardsUi.Actions = activity

    @Provides @ActivityScope
    fun provideRenderer(
            actions: MissingCardsUi.Actions,
            schedulers: AppSchedulers
    ) : MissingCardsRenderer = MissingCardsRenderer(actions, schedulers.main, schedulers.comp)
}
