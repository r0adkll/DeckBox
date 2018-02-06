package com.r0adkll.deckbuilder.arch.ui.features.browse

import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi.BrowseFilter.*
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
import io.reactivex.Scheduler


class SetBrowserRenderer(
        val actions: SetBrowserUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<SetBrowserUi.State>(main, comp) {

    override fun start() {

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

        disposables += state
                .map { s ->
                    s.cards.sortedBy { it.number.toInt() }
                            .filter { when(s.filter) {
                                ALL -> true
                                POKEMON -> it.supertype == SuperType.POKEMON
                                TRAINER -> it.supertype == SuperType.TRAINER
                                ENERGY -> it.supertype == SuperType.ENERGY
                            } }
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