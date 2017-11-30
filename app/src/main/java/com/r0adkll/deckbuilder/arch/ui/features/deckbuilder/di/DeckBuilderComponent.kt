package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di


import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchComponent
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(DeckBuilderModule::class)])
interface DeckBuilderComponent {

    fun inject(activity: DeckBuilderActivity)

    fun unifiedSearchComponentBuilder(): UnifiedSearchComponent.Builder
}