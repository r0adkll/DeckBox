package com.r0adkll.deckbuilder.arch.ui.features.setbrowser.di

import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserActivity
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserRenderer
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class SetBrowserModule(val activity: SetBrowserActivity) {

    @Provides
    @ActivityScope
    fun provideUi(): SetBrowserUi = activity

    @Provides
    @ActivityScope
    fun provideIntentions(): SetBrowserUi.Intentions = activity

    @Provides
    @ActivityScope
    fun provideActions(): SetBrowserUi.Actions = activity

    @Provides
    @ActivityScope
    fun provideRenderer(
        actions: SetBrowserUi.Actions,
        schedulers: AppSchedulers
    ): SetBrowserRenderer = SetBrowserRenderer(actions, schedulers.main, schedulers.comp)
}
