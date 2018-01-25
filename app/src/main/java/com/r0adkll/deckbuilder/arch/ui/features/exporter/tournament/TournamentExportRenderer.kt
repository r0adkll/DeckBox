package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import io.reactivex.Scheduler


class TournamentExportRenderer(
        val actions: TournamentExportUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<TournamentExportUi.State>(main, comp) {

    override fun start() {



    }
}