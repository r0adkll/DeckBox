package com.r0adkll.deckbuilder.arch.ui.features.collection.di

import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionFragment
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionProgressController
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionRenderer
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides

@Module
class CollectionModule(val fragment: CollectionFragment) {

    @Provides @FragmentScope
    fun provideUi(): CollectionUi = fragment

    @Provides @FragmentScope
    fun provideIntentions(): CollectionUi.Intentions = fragment

    @Provides @FragmentScope
    fun provideActions(): CollectionUi.Actions = fragment

    @Provides @FragmentScope
    fun provideRenderer(
            actions: CollectionUi.Actions,
            schedulers: AppSchedulers,
            controller: CollectionProgressController
    ): CollectionRenderer = CollectionRenderer(actions, schedulers, controller)
}
