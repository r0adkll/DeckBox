package com.r0adkll.deckbuilder.arch.ui.features.carddetail


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.fromReleaseDate
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class CardDetailRenderer(
        val actions: CardDetailUi.Actions,
        comp: Scheduler,
        main: Scheduler
) : DisposableStateRenderer<CardDetailUi.State>(main, comp) {

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
                    actions.showValidation(when{
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
                .mapNullable { it.products?.maxBy { it.recordedAt } }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showPrices(it.value?.price?.low, it.value?.price?.market,
                            it.value?.price?.high)
                }

        disposables += state
                .mapNullable { it.products?.sortedBy { it.recordedAt } }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    actions.showPriceHistory(it.value ?: emptyList())
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
    }

    fun List<PokemonCard>.sortByExpansionReleaseDate(): List<PokemonCard> = this.sortedByDescending {
        it.expansion?.releaseDate?.fromReleaseDate() ?: 0L
    }
}
