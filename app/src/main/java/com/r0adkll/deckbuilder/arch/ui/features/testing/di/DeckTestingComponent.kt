package com.r0adkll.deckbuilder.arch.ui.features.testing.di

import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(DeckTestingModule::class)])
interface DeckTestingComponent {

    fun inject(activity: DeckTestingActivity)
}
