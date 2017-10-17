package com.r0adkll.deckbuilder.arch.ui.features.search.di


import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchRenderer
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi
import com.r0adkll.deckbuilder.internal.di.ActivityScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class SearchModule(val activity: SearchActivity) {

    @Provides @ActivityScope
    fun provideUi(): SearchUi = activity


    @Provides @ActivityScope
    fun provideIntentions(): SearchUi.Intentions = activity


    @Provides @ActivityScope
    fun provideActions(): SearchUi.Actions = activity


    @Provides @ActivityScope
    fun provideRenderer(
            actions: SearchUi.Actions,
            schedulers: Schedulers
    ) : SearchRenderer = SearchRenderer(actions, schedulers.main, schedulers.comp)
}