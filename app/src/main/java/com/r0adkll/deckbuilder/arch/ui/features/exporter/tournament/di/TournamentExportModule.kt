package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.di


import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportFragment
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportRenderer
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class TournamentExportModule(val fragment: TournamentExportFragment) {

    @Provides @FragmentScope
    fun provideUi(): TournamentExportUi = fragment


    @Provides @FragmentScope
    fun provideIntentions(): TournamentExportUi.Intentions = fragment


    @Provides @FragmentScope
    fun provideActions(): TournamentExportUi.Actions = fragment


    @Provides @FragmentScope
    fun providRenderer(
        actions: TournamentExportUi.Actions,
        schedulers: Schedulers
    ) : TournamentExportRenderer = TournamentExportRenderer(actions, schedulers.main, schedulers.comp)
}