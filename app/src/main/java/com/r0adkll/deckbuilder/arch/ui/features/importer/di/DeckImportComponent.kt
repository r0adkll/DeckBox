package com.r0adkll.deckbuilder.arch.ui.features.importer.di

import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportActivity
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [DeckImportModule::class])
interface DeckImportComponent {

    fun inject(activity: DeckImportActivity)
}
