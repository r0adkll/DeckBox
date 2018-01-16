package com.r0adkll.deckbuilder.arch.ui.features.filter


import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.*
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterIntentions
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class FilterPresenter @Inject constructor(
        val ui: FilterUi,
        val intentions: FilterUi.Intentions,
        val repository: CardRepository,
        val categoryIntentions: FilterIntentions
) : Presenter() {

    override fun start() {

        val loadExpansions = repository.getExpansions()
                .onErrorReturnItem(emptyList())
                .map { it.asReversed() }
                .map { Change.ExpansionsLoaded(it) as Change }

        val typeSelected = intentions.typeClicks()
                .map { Change.TypeSelected(it.first, it.second) as Change }

        val attributeSelected = intentions.attributeClicks()
                .map { Change.AttributeSelected(it) as Change }

        val optionClicks = intentions.optionClicks()
                .map { option ->
                    val value = option.second
                    when(value) {
                        is Expansion -> Change.ExpansionSelected(value)
                        is Rarity -> Change.RaritySelected(value)
                        else -> Change.ExpansionSelected(value as Expansion)
                    }
                }

        val valueRangeChanges = intentions.valueRangeChanges()
                .map { Change.ValueRangeChanged(it.first, it.second.toFilter()) }

        val viewMoreSelected = intentions.viewMoreClicks()
                .map { Change.ViewMoreSelected as Change }

        val clearFilter = intentions.clearFilter()
                .map { Change.ClearFilter as Change }

        val categoryChanges = categoryIntentions.categoryChange()
                .map { Change.CategoryChanged(it) as Change }


        val merged = loadExpansions
                .mergeWith(typeSelected)
                .mergeWith(attributeSelected)
                .mergeWith(optionClicks)
                .mergeWith(valueRangeChanges)
                .mergeWith(viewMoreSelected)
                .mergeWith(clearFilter)
                .mergeWith(categoryChanges)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .doOnNext {
                    it.filters.forEach {
                        categoryIntentions.filterChanges().accept(Pair(it.key, it.value.filter))
                    }
                }
                .subscribe(ui::render, { t ->
                    Timber.e(t, "Error in filter reduction")
                })

    }
}