package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State.Change
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class CardDetailPresenter @Inject constructor(
        val ui: CardDetailUi,
        val intentions: CardDetailUi.Intentions,
        val repository: CardRepository,
        val validator: DeckValidator
) : Presenter() {

    override fun start() {


        val loadValidation = validator.validate(listOf(ui.state.card!!))
                .map { Change.Validated(it) as Change }

        val loadVariants = repository.search(ui.state.card!!.supertype, "\"${ui.state.card!!.name}\"")
                // FIXME: Replace with actual error implementation
                .onErrorReturnItem(emptyList())
                .map { it.filter { it.id != ui.state.card!!.id } }
                .map { Change.VariantsLoaded(it) as Change }

        val loadEvolves = ui.state.card!!.evolvesFrom?.let {
            // FIXME: Replace with actual error implementation
            repository.search(ui.state.card!!.supertype, "\"$it\"")
                    .onErrorReturnItem(emptyList())
                    .map { Change.EvolvesFromLoaded(it) as Change }
        } ?: Observable.empty()


        val addCard = intentions.addCardClicks()
                .map { Change.AddCard }

        val removeCard = intentions.removeCardClicks()
                .map { Change.RemoveCard }

        val updateDeck = intentions.updateDeck()
                .map { Change.DeckUpdated(it) }


        val merged = loadVariants
                .mergeWith(loadValidation)
                .mergeWith(loadEvolves)
                .mergeWith(addCard)
                .mergeWith(removeCard)
                .mergeWith(updateDeck)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }
    }
}