package com.r0adkll.deckbuilder.arch.ui.features.filter.di


import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterFragment
import com.r0adkll.deckbuilder.internal.di.scopes.FilterScope
import dagger.Subcomponent


@FilterScope
@Subcomponent(modules = arrayOf(FilterModule::class))
interface FilterComponent {

    fun inject(fragment: FilterFragment)
}