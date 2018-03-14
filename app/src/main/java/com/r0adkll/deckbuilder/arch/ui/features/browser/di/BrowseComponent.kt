package com.r0adkll.deckbuilder.arch.ui.features.browser.di

import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseFragment
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [(BrowseModule::class)])
interface BrowseComponent {

    fun inject(fragment: BrowseFragment)
}