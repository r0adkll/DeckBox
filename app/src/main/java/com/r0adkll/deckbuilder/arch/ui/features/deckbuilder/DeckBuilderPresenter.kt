package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State.*
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


@SuppressLint("CheckResult")
class DeckBuilderPresenter @Inject constructor(
        val ui: DeckBuilderUi,
        val intentions: DeckBuilderUi.Intentions,
        val repository: DeckRepository,
        val validator: DeckValidator
) : Presenter() {

    override fun start() {

        val loadDeck = intentions.loadDeck()
                .flatMap {
                    repository.getDeck(it)
                            .flatMap {
                                validator.validate(it.cards)
                                        .map { Change.Validated(it) as Change }
                                        .startWith(Change.DeckLoaded(it) as Change)
                            }
                }


        val initialValidation = validator.validate(ui.state.allCards)
                .map { Change.Validated(it) as Change }

        val addCard = intentions.addCards()
                .flatMap { cards ->
                    validator.validate(ui.state.allCards.plus(cards))
                            .map { Change.Validated(it) as Change }
                            .startWith(Change.AddCards(cards) as Change)
                            .onErrorReturn(handleUnknownError)
                }

        val removeCard = intentions.removeCard()
                .flatMap { card ->
                    validator.validate(ui.state.allCards.minus(card))
                            .map { Change.Validated(it) as Change }
                            .startWith(Change.RemoveCard(card) as Change)
                            .onErrorReturn(handleUnknownError)
                }

        val editCards = intentions.editCards()
                .flatMap { cards ->
                    validator.validate(cards)
                            .map { Change.Validated(it) as Change }
                            .startWith(Change.EditCards(cards) as Change)
                            .onErrorReturn(handleUnknownError)
                }

        val editDeck = intentions.editDeckClicks()
                .map { Change.Editing(it) as Change }

        val editName = intentions.editDeckName()
                .map { Change.EditName(it) as Change }

        val editDescription = intentions.editDeckDescription()
                .map { Change.EditDescription(it) as Change }

        val saveDeck = intentions.saveDeck()
                .flatMap { saveDeck() }

        val merged = initialValidation
                .mergeWith(loadDeck)
                .mergeWith(addCard)
                .mergeWith(removeCard)
                .mergeWith(editCards)
                .mergeWith(editDeck)
                .mergeWith(editName)
                .mergeWith(editDescription)
                .mergeWith(saveDeck)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe(ui::render)
    }


    private fun saveDeck(): Observable<Change> {
        val cards = ui.state.pokemonCards
                .plus(ui.state.trainerCards)
                .plus(ui.state.energyCards)
        val persistable =  if (ui.state.deck == null) {
            repository.createDeck(cards, ui.state.name ?: "", ui.state.description)
                    .map { Change.DeckUpdated(it) as Change }
        }
        else {
            repository.updateDeck(ui.state.deck!!.id, cards, ui.state.name ?: "", ui.state.description)
                    .map { Change.DeckUpdated(it) as Change }
        }

        return persistable.startWith(Change.Saving)
                .onErrorReturn(handleUnknownError)
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error processing deck")
            Change.Error(t.localizedMessage)
        }
    }
}