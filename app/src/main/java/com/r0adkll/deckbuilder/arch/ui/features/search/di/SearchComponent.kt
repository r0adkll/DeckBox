package com.r0adkll.deckbuilder.arch.ui.features.search.di

import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableComponent
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableModule
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SearchModule::class, FilterableModule::class])
interface SearchComponent : FilterableComponent {

    fun inject(activity: SearchActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build(): SearchComponent
        fun filterableModule(module: FilterableModule): Builder
        fun searchModule(module: SearchModule): Builder
    }
}
