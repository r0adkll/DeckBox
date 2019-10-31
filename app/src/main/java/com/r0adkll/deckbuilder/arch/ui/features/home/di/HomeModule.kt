package com.r0adkll.deckbuilder.arch.ui.features.home.di

import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionProgressController
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class HomeModule(val activity: HomeActivity) {

    @Provides @ActivityScope
    fun provideCollectionProgressController(): CollectionProgressController = activity
}
