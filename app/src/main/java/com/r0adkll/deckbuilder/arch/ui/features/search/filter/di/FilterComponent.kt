package com.r0adkll.deckbuilder.arch.ui.features.search.filter.di


import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterFragment
import com.r0adkll.deckbuilder.internal.di.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = arrayOf(FilterModule::class))
interface FilterComponent {

    fun inject(fragment: FilterFragment)
}