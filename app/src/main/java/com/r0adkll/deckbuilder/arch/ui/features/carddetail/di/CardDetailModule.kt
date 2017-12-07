package com.r0adkll.deckbuilder.arch.ui.features.carddetail.di

import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailRenderer
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class CardDetailModule(val activity: CardDetailActivity) {

    @Provides @ActivityScope
    fun provideUi(): CardDetailUi = activity


    @Provides @ActivityScope
    fun provideActions(): CardDetailUi.Actions = activity


    @Provides @ActivityScope
    fun provideRenderer(
            actions: CardDetailUi.Actions,
            schedulers: Schedulers
    ) : CardDetailRenderer = CardDetailRenderer(actions, schedulers.comp, schedulers.main)

}