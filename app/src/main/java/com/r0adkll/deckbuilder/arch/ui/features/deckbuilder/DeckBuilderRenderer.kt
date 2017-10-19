package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.util.ArrayMap
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DeckBuilderRenderer(
        val actions: DeckBuilderUi.Actions,
        val main: Scheduler,
        val comp: Scheduler
) : DisposableStateRenderer<DeckBuilderUi.State>() {

    override fun start() {

        disposables += state
                .map { it.pokemonCards }
                .distinctUntilChanged()
                .map(stackCards())
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showPokemonCards(it) }

        disposables += state
                .map { it.trainerCards }
                .distinctUntilChanged()
                .map(stackCards())
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showTrainerCards(it) }

        disposables += state
                .map { it.energyCards }
                .distinctUntilChanged()
                .map(stackCards())
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showEnergyCards(it) }

        disposables += state
                .mapNullable { it.name }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    it.value?.let {
                        actions.showDeckName(it)
                    }
                }

        disposables += state
                .mapNullable { it.name }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    it.value?.let {
                        actions.showDeckDescription(it)
                    }
                }
    }

    companion object {

        private fun stackCards(): (List<PokemonCard>) -> List<StackedPokemonCard> {
            return {
                val map = ArrayMap<PokemonCard, Int>(it.size)
                it.forEach { card ->
                    val count = map[card] ?: 0
                    map[card] = count + 1
                }
                map.map { StackedPokemonCard(it.key, it.value) }
                        .sortedBy { card -> card.card.nationalPokedexNumber }
            }
        }
    }
}