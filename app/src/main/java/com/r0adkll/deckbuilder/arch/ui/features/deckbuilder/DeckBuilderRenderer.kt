package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


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
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showPokemonCards(it) }

        disposables += state
                .map { it.trainerCards }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showTrainerCards(it) }

        disposables += state
                .map { it.energyCards }
                .distinctUntilChanged()
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
}