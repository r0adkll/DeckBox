package com.r0adkll.deckbuilder.arch.ui.features.carddetail


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class CardDetailRenderer(
        val actions: CardDetailUi.Actions,
        val comp: Scheduler,
        val main: Scheduler
) : DisposableStateRenderer<CardDetailUi.State>() {

    override fun start() {

        disposables += state
                .map { it.variants }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showVariants(it) }

        disposables += state
                .map { it.evolvesFrom }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showEvolvesFrom(it) }
    }
}