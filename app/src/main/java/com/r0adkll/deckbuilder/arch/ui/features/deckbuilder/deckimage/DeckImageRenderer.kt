package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.extensions.combine
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import io.reactivex.Scheduler


class DeckImageRenderer(
        val actions: DeckImageUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<DeckImageUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
                .map { it.cards }
                .map { it ->
                    val cards = it.filter { it.supertype == SuperType.POKEMON }
                    val stacks = CardUtils.stackCards().invoke(cards)
                    val evolutions = EvolutionChain.build(stacks)
                    val types = stacks.flatMap { it.card.types ?: emptyList() }
                            .fold(HashSet<Type>()) { acc, type ->
                                acc.add(type)
                                acc
                            }

                    val images = ArrayList<DeckImage>()

                    // Add pokemon deck images
                    evolutions.forEach {
                        it.last()?.cards?.firstOrNull()?.let {
                            images += DeckImage.Pokemon(it.card.imageUrlHiRes)
                        }
                    }

                    // Add type deck images
                    val combinations = types.combine(types)
                    images += combinations.map {
                        if (it.first == it.second) {
                            DeckImage.Type(it.first, null)
                        } else {
                            DeckImage.Type(it.first, it.second)
                        }
                    }

                    images
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setDeckImages(it) }

        disposables += state
                .mapNullable { it.selectedDeckImage }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setSelectedDeckImage(it.value) }

        disposables += state
                .map { it.isSaved }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it) {
                        actions.close()
                    }
                }

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
    }
}