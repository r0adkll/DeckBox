package com.r0adkll.deckbuilder.arch.ui.features.importer

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler

class DeckImportRenderer(
        actions: DeckImportUi.Actions,
        comp: Scheduler,
        main: Scheduler
) : UiBaseStateRenderer<DeckImportUi.State, DeckImportUi.State.Change, DeckImportUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
                .map { it.cards }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setResults(it) }
    }
}
