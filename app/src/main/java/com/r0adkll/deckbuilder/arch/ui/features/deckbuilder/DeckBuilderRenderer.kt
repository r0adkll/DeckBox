package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.DisposableStateRenderer
import com.ftinc.kit.arch.util.mapNullable
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderRenderer.SumType.EXCLUDE_COLLECTION
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderRenderer.SumType.NO_COLLECTION
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderRenderer.SumType.ONLY_COLLECTION
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.Scheduler

class DeckBuilderRenderer(
    val actions: DeckBuilderUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : DisposableStateRenderer<DeckBuilderUi.State>(main, comp) {

    @Suppress("LongMethod")
    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
            .map {
                when {
                    it.validation.standard -> Format.STANDARD
                    it.validation.expanded -> Format.EXPANDED
                    else -> Format.UNLIMITED
                }
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showFormat(it) }

        disposables += state
            .map { it.validation.rules }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showBrokenRules(it) }

        disposables += state
            .map { it.isEditing }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showIsEditing(it) }

        disposables += state
            .map { it.isOverview }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showIsOverview(it) }

        disposables += state
            .mapNullable { it.error }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                val value = it.value
                if (value != null) {
                    actions.showError(value)
                }
            }

        disposables += state
            .map { it.pokemonCards.stack(it.collectionCounts) }
            .distinctUntilChanged { t1, t2 ->
                t1.hashCode() == t2.hashCode()
            }
            .addToLifecycle()
            .subscribe { actions.showPokemonCards(it) }

        disposables += state
            .map { it.trainerCards.stack(it.collectionCounts) }
            .distinctUntilChanged { t1, t2 ->
                t1.hashCode() == t2.hashCode()
            }
            .addToLifecycle()
            .subscribe { actions.showTrainerCards(it) }

        disposables += state
            .map { it.energyCards.stack(it.collectionCounts) }
            .distinctUntilChanged { t1, t2 ->
                t1.hashCode() == t2.hashCode()
            }
            .addToLifecycle()
            .subscribe { actions.showEnergyCards(it) }

        disposables += state
            .map { it.pokemonCards.size + it.trainerCards.size + it.energyCards.size }
            .distinctUntilChanged()
            .subscribeOn(comp)
            .observeOn(main)
            .subscribe { actions.showCardCount(it) }

        disposables += state
            .mapNullable { it.name }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                it.value?.let {
                    actions.showDeckName(it)
                }
            }

        disposables += state
            .mapNullable { it.description }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { desc ->
                desc.value?.let {
                    actions.showDeckDescription(it)
                }
            }

        disposables += state
            .mapNullable { it.image }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showDeckImage(it.value) }

        disposables += state
            .map { it.collectionOnly }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showDeckCollectionOnly(it) }

        disposables += state
            .mapNullable { buildMarketPrices(it) }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { marketPrice ->
                actions.showPrices(
                    marketPrice.value?.low,
                    marketPrice.value?.market,
                    marketPrice.value?.high
                )
                actions.showCollectionPrices(
                    marketPrice.value?.collectionLow,
                    marketPrice.value?.collectionMarket,
                    marketPrice.value?.collectionHigh
                )
            }
    }

    private enum class SumType {
        NO_COLLECTION,
        EXCLUDE_COLLECTION,
        ONLY_COLLECTION
    }

    private class MarketPrices(
        val low: Double?,
        val market: Double?,
        val high: Double?,
        val collectionLow: Double? = null,
        val collectionMarket: Double? = null,
        val collectionHigh: Double? = null
    )

    private fun buildMarketPrices(state: DeckBuilderUi.State): MarketPrices? {
        return if (state.products.isNotEmpty()) {
            val sumType = if (state.collectionOnly) EXCLUDE_COLLECTION else NO_COLLECTION
            val allCards = state.allCardsStackedWithCollection

            val low = allCards.computeSum(state.products, sumType) { it.latestPrice?.low }
            val market = allCards.computeSum(state.products, sumType) { it.latestPrice?.market }
            val high = allCards.computeSum(state.products, sumType) { it.latestPrice?.high }

            val lowCollection = computeIf(state.collectionOnly) {
                allCards.computeSum(state.products, ONLY_COLLECTION) { it.latestPrice?.low }
            }
            val marketCollection = computeIf(state.collectionOnly) {
                allCards.computeSum(state.products, ONLY_COLLECTION) { it.latestPrice?.market }
            }
            val highCollection = computeIf(state.collectionOnly) {
                allCards.computeSum(state.products, ONLY_COLLECTION) { it.latestPrice?.high }
            }

            MarketPrices(low, market, high,
                lowCollection, marketCollection, highCollection)
        } else {
            null
        }
    }

    private fun computeIf(value: Boolean, compute: () -> Double): Double? {
        return if (value) compute() else null
    }

    private fun List<StackedPokemonCard>.computeSum(
        products: Map<String, Product>,
        sumType: SumType = NO_COLLECTION,
        selector: (Product) -> Double?
    ): Double {
        return sumByDouble {
            val count = when (sumType) {
                NO_COLLECTION -> it.count
                EXCLUDE_COLLECTION -> (it.count - (it.collection ?: 0)).coerceAtLeast(0)
                ONLY_COLLECTION -> it.collection?.coerceIn(0, it.count) ?: 0
            }
            val product = products[it.card.id]
            val price = product?.let(selector) ?: 0.0
            price * count
        }
    }
}
