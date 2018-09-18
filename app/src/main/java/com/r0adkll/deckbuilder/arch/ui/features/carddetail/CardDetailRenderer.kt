package com.r0adkll.deckbuilder.arch.ui.features.carddetail


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class CardDetailRenderer(
        val actions: CardDetailUi.Actions,
        comp: Scheduler,
        main: Scheduler
) : DisposableStateRenderer<CardDetailUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
                .mapNullable { it.count }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showCopies(it.value) }

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