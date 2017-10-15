package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di


import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.internal.di.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(DeckBuilderModule::class))
interface DeckBuilderComponent {

    fun inject(activity: DeckBuilderActivity)
}