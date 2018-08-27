package com.r0adkll.deckbuilder.arch.ui.features.overview

import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class OverviewRenderer(
        actions: OverviewUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<OverviewUi.State, OverviewUi.State.Change, OverviewUi.Actions>(actions, main, comp) {

    override fun onStart() {

        disposables += state
                .map { it.cards }
                .map(CardUtils.stackCards())
                .map { EvolutionChain.build(it) }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showCards(it) }
    }
}