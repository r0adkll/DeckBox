package com.r0adkll.deckbuilder.arch.ui.features.exporter.di


import com.r0adkll.deckbuilder.arch.ui.features.exporter.MultiExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.di.TournamentExportComponent
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.di.TournamentExportModule
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = [(MultiExportModule::class)])
interface MultiExportComponent {

    fun inject(activity: MultiExportActivity)
    fun plus(module: TournamentExportModule): TournamentExportComponent
}