package com.r0adkll.deckbuilder.arch.ui.features.collection.set.di

import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [CollectionSetModule::class])
interface CollectionSetComponent {

    fun inject(activity: CollectionSetActivity)
}
