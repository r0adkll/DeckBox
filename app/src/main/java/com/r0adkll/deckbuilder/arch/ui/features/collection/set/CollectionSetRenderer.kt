package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.extensions.sortableNumber
import io.reactivex.functions.BiPredicate


class CollectionSetRenderer(
        actions: CollectionSetUi.Actions,
        schedulers: AppSchedulers
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
                .map { it.onlyMissingCards }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showOnlyMissingCards(it)
                }

        disposables += state
                .map { s ->
                    s.cards
                            .sortedBy { it.sortableNumber }.map { card ->
                                val count = s.counts.find { it.id == card.id && !it.isSourceOld }
                                StackedPokemonCard(card, count?.count ?: 0)
                            }
                            .filter { stackedCard ->
                                if (s.onlyMissingCards) {
                                    stackedCard.count == 0
                                } else {
                                    true
                                }
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
