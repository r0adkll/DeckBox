package com.r0adkll.deckbuilder.arch.ui.features.missingcards

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler

class MissingCardsRenderer(
        val actions: MissingCardsUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<MissingCardsUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
                .map { it.expansions }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setExpansions(it) }

        disposables += state
                .mapNullable { it.expansion }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setExpansion(it.value) }

        disposables += state
                .mapNullable { it.name }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setName(it.value) }

        disposables += state
                .mapNullable { it.setNumber }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setNumber(it.value) }

        disposables += state
                .mapNullable { it.description }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setDescription(it.value) }

        disposables += state
                .map { it.print }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setPrint(it) }

        disposables += state
                .map { it.reportSubmitted }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it) {
                        actions.closeReport()
                    }
                }

        disposables += state
                .map { it.isReportReady }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setSendEnabled(it) }

        disposables += state
                .map { it.isLoading }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showLoading(it) }

        disposables += state
                .mapNullable { it.error }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.showError(it.value)
                    } else {
                        actions.hideError()
                    }
                }

    }
}
