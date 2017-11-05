package com.r0adkll.deckbuilder.arch.ui.features.search

import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
import io.reactivex.Scheduler


class SearchRenderer(
        val actions: SearchUi.Actions,
        val main: Scheduler,
        val comp: Scheduler
) : DisposableStateRenderer<SearchUi.State>() {

    override fun start() {

        subscribeToSuperType(SuperType.POKEMON)
        subscribeToSuperType(SuperType.TRAINER)
        subscribeToSuperType(SuperType.ENERGY)

        // Subscribe to query text changes
        disposables += state
                .map { Pair(it.category, it.results[it.category]?.query ?: "") }
                .distinctUntilChanged { t -> t.first }
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    actions.setQueryText(it.second)
                }

        // Subscribe to the category change itself
        disposables += state
                .map { it.category }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setCategory(it) }


        disposables += state
                .map { it.selected }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setSelectedCards(it) }
    }


    private fun subscribeToSuperType(superType: SuperType) {

        disposables += state
                .mapNullable { it.results[superType]?.filter?.isEmpty ?: true }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showFilterEmpty(value)
                    }
                }

        disposables += state
                .mapNullable { it.results[superType]?.results }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.setResults(superType, value)
                    }
                }

        disposables += state
                .mapNullable { it.results[superType]?.isLoading }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showLoading(superType, value)
                    }
                }

        disposables += state
                .mapNullable { it.results[superType]?.error }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showError(superType, value)
                    } else {
                        actions.hideError(superType)
                    }
                }
    }
}