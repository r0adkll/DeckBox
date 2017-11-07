package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di


import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchFragment
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchComponent
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchModule
import com.r0adkll.deckbuilder.internal.di.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(DeckBuilderModule::class))
interface DeckBuilderComponent {

    fun inject(activity: DeckBuilderActivity)
    fun plus(module: UnifiedSearchModule): UnifiedSearchComponent
}