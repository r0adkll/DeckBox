package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.google.common.collect.Collections2
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.functions.BiPredicate
import java.util.*


class CollectionSetRenderer(
        actions: CollectionSetUi.Actions,
        schedulers: Schedulers
) : UiBaseStateRenderer<CollectionSetUi.State, CollectionSetUi.State.Change, CollectionSetUi.Actions>(actions, schedulers.main, schedulers.comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
                .map { s ->
                    val setCount = s.counts.filter { it.set == s.expansion!!.code && it.count > 0 && !it.isSourceOld }
                    setCount.size.toFloat() / s.expansion!!.totalCards.toFloat()
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showOverallProgress(it)
                }

        disposables += state
                .map { s ->
                    s.cards.sortedBy { it.number.replace("a", "", true).toIntOrNull() ?: 0 }.map { card ->
                        val count = s.counts.find { it.id == card.id && !it.isSourceOld }
                        StackedPokemonCard(card, count?.count ?: 0)
                    }
                }
                .distinctUntilChanged(BiPredicate { t1, t2 ->
                    t1.map { it.card to it.count } == t2.map { it.card to it.count }
                })
                .addToLifecycle()
                .subscribe {
                    actions.showCollection(it)
                }

    }
}