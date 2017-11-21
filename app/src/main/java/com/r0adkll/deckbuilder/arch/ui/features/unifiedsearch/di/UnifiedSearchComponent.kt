package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di


import com.r0adkll.deckbuilder.arch.ui.features.search.filter.di.FilterableComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.di.FilterableModule
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchFragment
import com.r0adkll.deckbuilder.internal.di.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = arrayOf(
        UnifiedSearchModule::class,
        FilterableModule::class
))
interface UnifiedSearchComponent : FilterableComponent{

    fun inject(fragment: SearchFragment)


    interface Builder {
        fun build(): UnifiedSearchComponent
        fun unifiedSearchModule(module: UnifiedSearchModule): Builder
        fun filterableModule(module: FilterableModule): Builder
    }
}