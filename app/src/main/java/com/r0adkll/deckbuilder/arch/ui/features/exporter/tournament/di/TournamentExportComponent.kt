package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.di


import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportFragment
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [(TournamentExportModule::class)])
interface TournamentExportComponent {

    fun inject(fragment: TournamentExportFragment)
}