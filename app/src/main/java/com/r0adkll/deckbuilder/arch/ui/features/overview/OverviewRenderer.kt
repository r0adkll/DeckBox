package com.r0adkll.deckbuilder.arch.ui.features.overview

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.mapNullable
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.EvolutionChainComparator
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.Scheduler

class OverviewRenderer(
    actions: OverviewUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : UiBaseStateRenderer<OverviewUi.State, OverviewUi.State.Change, OverviewUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
            .map { it.cards.stack() }
            .map { EvolutionChain.build(it) }
            .map { it.sortedWith(EvolutionChainComparator) }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showCards(it) }
    }
}
