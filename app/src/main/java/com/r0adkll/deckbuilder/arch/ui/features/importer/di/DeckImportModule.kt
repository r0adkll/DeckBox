package com.r0adkll.deckbuilder.arch.ui.features.importer.di


import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportActivity
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportRenderer
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportUi
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import dagger.Module
import dagger.Provides


@Module
class DeckImportModule(val activity: DeckImportActivity) {


    @Provides @ActivityScope
    fun provideUi(): DeckImportUi = activity


    @Provides @ActivityScope
    fun provideIntentions(): DeckImportUi.Intentions = activity


    @Provides @ActivityScope
    fun provideActions(): DeckImportUi.Actions = activity


    @Provides @ActivityScope
    fun provideRenderer(
            actions: DeckImportUi.Actions,
            schedulers: AppSchedulers
    ) : DeckImportRenderer = DeckImportRenderer(actions, schedulers.comp, schedulers.main)
}
