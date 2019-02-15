package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.util.Schedulers


class CollectionSetRenderer(
        actions: CollectionSetUi.Actions,
        schedulers: Schedulers
) : UiBaseStateRenderer<CollectionSetUi.State, CollectionSetUi.State.Change, CollectionSetUi.Actions>(actions, schedulers.main, schedulers.comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
                .map { s ->
                    val setCount = s.counts.filter { it.set == s.expansion!!.code }
                    s.expansion!!.totalCards.toFloat() / setCount.size.toFloat()
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showOverallProgress(it)
                }

        disposables += state
                .map { s ->
                    s.cards.map { card ->
                        val count = s.counts.find { it.id == card.id }
                        StackedPokemonCard(card, count?.count ?: 0)
                    }
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showCollection(it)
                }

    }
}