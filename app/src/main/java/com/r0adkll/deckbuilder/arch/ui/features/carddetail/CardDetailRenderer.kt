package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.DisposableStateRenderer
import com.ftinc.kit.arch.util.mapNullable
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.extensions.fromReleaseDate
import io.reactivex.Scheduler

class CardDetailRenderer(
    val actions: CardDetailUi.Actions,
    comp: Scheduler,
    main: Scheduler
) : DisposableStateRenderer<CardDetailUi.State>(main, comp) {

    @Suppress("LongMethod")
    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
            .mapNullable { it.count }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showCopies(it.value) }

        disposables += state
            .map { it.validation }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.showValidation(when {
                    it.standard -> Format.STANDARD
                    it.expanded -> Format.EXPANDED
                    else -> Format.UNLIMITED
                })
            }

        disposables += state
            .map { it.collectionCount }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.showCollectionCount(it)
            }

        disposables += state
            .mapNullable { it.product?.prices?.maxBy { it.updatedAt } }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.showPrices(it.value?.low, it.value?.market,
                    it.value?.high)
            }

        disposables += state
            .mapNullable { it.product}
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                val prices = it.value?.prices?.sortedBy { it.updatedAt } ?: emptyList()
                actions.showPriceHistory(it.value, prices)
            }

        disposables += state
            .map { it.variants.sortByExpansionReleaseDate() }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showVariants(it) }

        disposables += state
            .map { it.evolvesFrom.sortByExpansionReleaseDate() }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showEvolvesFrom(it) }

        disposables += state
            .map { it.evolvesTo.sortByExpansionReleaseDate() }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.showEvolvesTo(it) }

        disposables += state
            .mapNullable { it.card }
            .filter { it.value?.isPreview == true }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                if (it.isNonNull()) {
                    actions.hideCollectionCounter()
                    actions.showCardInformation(it.value!!)
                }
            }
    }

    private fun List<PokemonCard>.sortByExpansionReleaseDate(): List<PokemonCard> = this.sortedByDescending {
        it.expansion?.releaseDate?.fromReleaseDate() ?: 0L
    }
}
