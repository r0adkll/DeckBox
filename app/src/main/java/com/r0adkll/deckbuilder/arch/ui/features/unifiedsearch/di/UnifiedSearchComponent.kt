package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di


import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchFragment
import com.r0adkll.deckbuilder.internal.di.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = arrayOf(UnifiedSearchModule::class))
interface UnifiedSearchComponent {

    fun inject(fragment: SearchFragment)
}