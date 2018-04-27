package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DeckTestingRenderer(
        actions: DeckTestingUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<DeckTestingUi.State, DeckTestingUi.State.Change, DeckTestingUi.Actions>(actions, main, comp) {


    override fun onStart() {

        disposables += state
                .mapNullable { it.results }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.showTestResults(it.value)
                    }
                }
    }
}