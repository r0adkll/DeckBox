package com.r0adkll.deckbuilder.arch.ui.features.search

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
import io.reactivex.Scheduler

class SearchRenderer(
        val actions: SearchUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<SearchUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        subscribeToSuperType(SuperType.POKEMON)
        subscribeToSuperType(SuperType.TRAINER)
        subscribeToSuperType(SuperType.ENERGY)

        // Subscribe to query text changes
        disposables += state
                .map { Pair(it.category, it.results[it.category]?.query ?: "") }
                .distinctUntilChanged { t -> t.first }
                .addToLifecycle()
                .subscribe {
                    actions.setQueryText(it.second)
                }

        // Subscribe to the category change itself
        disposables += state
                .map { it.category }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setCategory(it) }

        disposables += state
                .map { it.selected }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setSelectedCards(it) }
    }

    @SuppressLint("RxSubscribeOnError")
    private fun subscribeToSuperType(superType: SuperType) {

        disposables += state
                .mapNullable { it.results[superType]?.filter?.isEmpty ?: true }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showFilterEmpty(value)
                    }
                }

        disposables += state
                .mapNullable { it.results[superType]?.results }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.setResults(superType, value)
                    }
                }

        disposables += state
                .mapNullable { it.results[superType]?.isLoading }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showLoading(superType, value)
                    }
                }

        disposables += state
                .mapNullable { it.results[superType]?.error }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showError(superType, value)
                    } else {
                        actions.hideError(superType)
                    }
                }

        disposables += state
                .map {
                    val results = it.results[superType]!!
                    results.results.isEmpty() && !results.isLoading && results.query.isNotBlank()
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it) {
                        actions.showEmptyResults(superType)
                    } else {
                        actions.showEmptyDefault(superType)
                    }
                }
    }
}
