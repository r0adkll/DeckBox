package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.dateOfBirth
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler

class TournamentExportRenderer(
        val actions: TournamentExportUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<TournamentExportUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
                .mapNullable { it.playerName }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setPlayerName(it.value) }

        disposables += state
                .mapNullable { it.playerId }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setPlayerId(it.value) }

        disposables += state
                .mapNullable { it.dob?.dateOfBirth() }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setDateOfBirth(it.value) }

        disposables += state
                .mapNullable { it.ageDivision }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setAgeDivision(it.value) }

        disposables += state
                .mapNullable { it.format }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setFormat(it.value) }

    }
}
