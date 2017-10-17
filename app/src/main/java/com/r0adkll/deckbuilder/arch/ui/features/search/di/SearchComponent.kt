package com.r0adkll.deckbuilder.arch.ui.features.search.di


import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.internal.di.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(SearchModule::class))
interface SearchComponent {

    fun inject(activity: SearchActivity)
}