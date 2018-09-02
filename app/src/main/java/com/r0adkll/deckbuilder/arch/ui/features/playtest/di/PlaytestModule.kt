package com.r0adkll.deckbuilder.arch.ui.features.playtest.di


import com.r0adkll.deckbuilder.arch.ui.features.playtest.PlaytestActivity
import com.r0adkll.deckbuilder.arch.ui.features.playtest.PlaytestRenderer
import com.r0adkll.deckbuilder.arch.ui.features.playtest.PlaytestUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class PlaytestModule(val activity: PlaytestActivity) {

    @Provides @ActivityScope
    fun provideUi(): PlaytestUi = activity


    @Provides @ActivityScope
    fun provideIntentions(): PlaytestUi.Intentions = activity


    @Provides @ActivityScope
    fun provideActions(): PlaytestUi.Actions = activity


    @Provides @ActivityScope
    fun provideRenderer(
            actions: PlaytestUi.Actions,
            schedulers: Schedulers
    ): PlaytestRenderer = PlaytestRenderer(actions, schedulers.main, schedulers.comp)
}