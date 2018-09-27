package com.r0adkll.deckbuilder.arch.ui.features.decks.di


import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksFragment
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [DecksModule::class])
interface DecksComponent {

    fun inject(fragment: DecksFragment)
}