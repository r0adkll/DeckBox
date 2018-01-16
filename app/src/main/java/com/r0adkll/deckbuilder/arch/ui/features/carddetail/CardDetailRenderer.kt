package com.r0adkll.deckbuilder.arch.ui.features.carddetail


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class CardDetailRenderer(
        val actions: CardDetailUi.Actions,
        comp: Scheduler,
        main: Scheduler
) : DisposableStateRenderer<CardDetailUi.State>(main, comp) {

    override fun start() {

        disposables += state
                .mapNullable { s ->
                    s.deck?.filter { it.id == s.card?.id }
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { cards ->
                    actions.showCopies(cards.value?.size)
                }

        disposables += state
                .mapNullable { it.deck }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setEditResults(it.value) }

        disposables += state
                .map { it.validation }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showStandardValidation(it.standard)
                    actions.showExpandedValidation(it.expanded)
                }

        disposables += state
                .map { it.variants }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showVariants(it) }

        disposables += state
                .map { it.evolvesFrom }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showEvolvesFrom(it) }
    }
}