package com.r0adkll.deckbuilder.arch.ui.features.overview.di

import com.r0adkll.deckbuilder.arch.ui.features.overview.OverviewFragment
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [OverviewModule::class])
interface OverviewComponent {

    fun inject(fragment: OverviewFragment)
}
