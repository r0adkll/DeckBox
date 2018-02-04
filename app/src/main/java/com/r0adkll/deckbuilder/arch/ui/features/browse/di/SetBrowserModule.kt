package com.r0adkll.deckbuilder.arch.ui.features.browse.di

import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserActivity
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserRenderer
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class SetBrowserModule(val activity: SetBrowserActivity) {

    @Provides @ActivityScope
    fun provideUi(): SetBrowserUi = activity


    @Provides @ActivityScope
    fun provideIntentions(): SetBrowserUi.Intentions = activity


    @Provides @ActivityScope
    fun provideActions(): SetBrowserUi.Actions = activity


    @Provides @ActivityScope
    fun provideRenderer(
            actions: SetBrowserUi.Actions,
            schedulers: Schedulers
    ) : SetBrowserRenderer = SetBrowserRenderer(actions, schedulers.main, schedulers.comp)
}