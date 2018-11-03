package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.CardUtils.stackCards
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DeckBuilderRenderer(
        val actions: DeckBuilderUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<DeckBuilderUi.State>(main, comp) {

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
                .map { it.isChanged }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showSaveAction(it) }

        disposables += state
                .map { it.isSaving }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showIsSaving(it) }

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
                .map { it.pokemonCards }
                .distinctUntilChanged()
                .map(stackCards())
                .addToLifecycle()
                .subscribe { actions.showPokemonCards(it) }

        disposables += state
                .map { it.trainerCards }
                .distinctUntilChanged()
                .map(stackCards())
                .addToLifecycle()
                .subscribe { actions.showTrainerCards(it) }

        disposables += state
                .map { it.energyCards }
                .distinctUntilChanged()
                .map(stackCards())
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
    }
}