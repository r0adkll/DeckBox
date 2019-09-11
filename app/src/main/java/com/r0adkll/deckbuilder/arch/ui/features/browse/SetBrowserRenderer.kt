package com.r0adkll.deckbuilder.arch.ui.features.browse

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi.BrowseFilter.*
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.sortableNumber
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Scheduler


class SetBrowserRenderer(
        val actions: SetBrowserUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<SetBrowserUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
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
                    val filtersToHide = ArrayList<SetBrowserUi.BrowseFilter>()
                    if (s.setCode.contains("sm", true)) {
                        val number = s.setCode.replace("sm", "").toIntOrNull()
                        if (number?.let { it >= 5 && it != 35 } != true) {
                            filtersToHide += PRISM
                        }
                        if (number?.let { it >= 9 && it != 35  } != true) {
                            filtersToHide += TAG_TEAM
                        }
                    } else {
                        filtersToHide += GX
                        filtersToHide += PRISM
                        filtersToHide += TAG_TEAM
                    }

                    filtersToHide
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.isNotEmpty()) {
                        actions.hideFilters(*it.toTypedArray())
                    }
                }

        disposables += state
                .map { s ->
                    s.cards.sortedBy { it.sortableNumber }
                            .filter { when(s.filter) {
                                ALL -> true
                                POKEMON -> it.supertype == SuperType.POKEMON
                                TRAINER -> it.supertype == SuperType.TRAINER
                                ENERGY -> it.supertype == SuperType.ENERGY
                                GX -> it.subtype == SubType.GX
                                TAG_TEAM -> it.subtype == SubType.TAG_TEAM
                                PRISM -> it.name.contains("◇")
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
