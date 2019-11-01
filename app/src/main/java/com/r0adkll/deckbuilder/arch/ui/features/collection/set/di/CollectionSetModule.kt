package com.r0adkll.deckbuilder.arch.ui.features.collection.set.di

import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetActivity
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetRenderer
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class CollectionSetModule(val activity: CollectionSetActivity) {

    @Provides
    @ActivityScope
    fun provideUi(): CollectionSetUi = activity

    @Provides
    @ActivityScope
    fun provideIntentions(): CollectionSetUi.Intentions = activity

    @Provides
    @ActivityScope
    fun provideActions(): CollectionSetUi.Actions = activity

    @Provides
    @ActivityScope
    fun provideRenderer(
        actions: CollectionSetUi.Actions,
        schedulers: AppSchedulers
    ): CollectionSetRenderer = CollectionSetRenderer(actions, schedulers)
}
