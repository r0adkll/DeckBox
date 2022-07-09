package com.r0adkll.deckbuilder.arch.ui.features.filter

import com.ftinc.kit.arch.presentation.presenter.Presenter
import com.ftinc.kit.arch.util.logState
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.subtypes.SubtypeRepository
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterIntentions
import io.reactivex.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class FilterPresenter @Inject constructor(
  val ui: FilterUi,
  val intentions: FilterUi.Intentions,
  val expansionRepository: ExpansionRepository,
  val subtypeRepository: SubtypeRepository,
  val categoryIntentions: FilterIntentions
) : Presenter() {

  override fun start() {

    val loadExpansions = expansionRepository.getExpansions()
      .onErrorReturnItem(emptyList())
      .map { it.asReversed() }
      .map { Change.ExpansionsLoaded(it) as Change }

    val loadSubTypes = subtypeRepository.getSubTypes()
      .onErrorReturnItem(emptyList())
      .map { Change.SubtypesLoaded(it) as Change }

    val fieldChanged = intentions.fieldChanges()
      .map { Change.FieldChanged(it) as Change }

    val typeSelected = intentions.typeClicks()
      .map { Change.TypeSelected(it.first, it.second) as Change }

    val attributeSelected = intentions.attributeClicks()
      .map { Change.AttributeSelected(it) as Change }

    val optionClicks = intentions.optionClicks()
      .map { option ->
        when (val value = option.second) {
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

    val merged = loadExpansions
      .mergeWith(loadSubTypes)
      .mergeWith(fieldChanged)
      .mergeWith(typeSelected)
      .mergeWith(attributeSelected)
      .mergeWith(optionClicks)
      .mergeWith(valueRangeChanges)
      .mergeWith(viewMoreSelected)
      .mergeWith(clearFilter)
      .doOnNext { Timber.d(it.logText) }

    disposables += merged.scan(ui.state, State::reduce)
      .logState()
      .doOnNext {
        categoryIntentions.filterChanges().accept(it.filter)
      }
      .subscribe(ui::render) { t ->
        Timber.e(t, "Error in filter reduction")
      }
  }
}
