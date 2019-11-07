package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.util.extensions.sortableNumber
import io.reactivex.Scheduler

class SetBrowserRenderer(
    actions: SetBrowserUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : UiBaseStateRenderer<SetBrowserUi.State, SetBrowserUi.State.Change, SetBrowserUi.Actions>(actions, main, comp) {

    @Suppress("MagicNumber")
    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
            .map { s ->
                BrowseFilter.findAvailable(s.cards)
                    .sortedBy { it.weight }
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.setFilters(it)
            }

        disposables += state
            .map { s ->
                s.cards
                    .sortedBy { it.sortableNumber }
                    .filter {
                        s.filter.apply(it)
                    }
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.setCards(it) }

        disposables += state
            .map { it.filter }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.setFilter(it) }
    }
}
