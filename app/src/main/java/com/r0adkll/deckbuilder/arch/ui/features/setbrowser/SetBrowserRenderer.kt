package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.ALL
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.ENERGY
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.GX
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.POKEMON
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.PRISM
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.TAG_TEAM
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.BrowseFilter.TRAINER
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.sortableNumber
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Scheduler

class SetBrowserRenderer(
        actions: SetBrowserUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<SetBrowserUi.State, SetBrowserUi.State.Change, SetBrowserUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

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
