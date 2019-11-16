package com.r0adkll.deckbuilder.arch.ui.features.settings.cache.di

import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheActivity
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheRenderer
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class ManageCacheModule(val activity: ManageCacheActivity) {

    @Provides @ActivityScope
    fun provideUi(): ManageCacheUi = activity

    @Provides @ActivityScope
    fun provideIntentions(): ManageCacheUi.Intentions = activity

    @Provides @ActivityScope
    fun provideActions(): ManageCacheUi.Actions = activity

    @Provides @ActivityScope
    fun provideRenderer(
        actions: ManageCacheUi.Actions,
        schedulers: AppSchedulers
    ): ManageCacheRenderer = ManageCacheRenderer(actions, schedulers)
}
