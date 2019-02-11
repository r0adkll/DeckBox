package com.r0adkll.deckbuilder.arch.ui.features.collection

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.ui.components.ExpansionComparator
import com.r0adkll.deckbuilder.arch.ui.features.collection.adapter.Item
import com.r0adkll.deckbuilder.util.Schedulers
import com.r0adkll.deckbuilder.util.extensions.fromReleaseDate
import com.r0adkll.deckbuilder.util.extensions.iso8601
import com.r0adkll.deckbuilder.util.extensions.plusAssign


class CollectionRenderer(
        actions: CollectionUi.Actions,
        schedulers: Schedulers
) : UiBaseStateRenderer<CollectionUi.State, CollectionUi.State.Change, CollectionUi.Actions>(actions, schedulers.main, schedulers.comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
                .map { s ->
                    val items = ArrayList<Item>()
                    val series = s.expansions
                            .sortedWith(ExpansionComparator())
                            .groupBy { it.series }
                    series.forEach { (series, expansions) ->
                        val seriesCount = s.counts.filter { it.series == series }.size
                        val seriesTotal = expansions.sumBy { it.totalCards }
                        val seriesCompletion = seriesCount.toFloat() / seriesTotal.toFloat()
                        items += Item.ExpansionSeries(series, seriesCompletion)
                        items += expansions.map { expansion ->
                            val setCount = s.counts.filter { it.set == expansion.code }.size
                            Item.ExpansionSet(expansion, setCount)
                        }
                    }
                    items
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.setItems(it)
                }

    }
}