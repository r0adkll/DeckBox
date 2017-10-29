package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State.*
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class DeckBuilderPresenter @Inject constructor(
        val ui: DeckBuilderUi,
        val intentions: DeckBuilderUi.Intentions,
        val repository: DeckRepository
) : Presenter() {

    override fun start() {

        val addCard = intentions.addCards()
                .map { Change.AddCards(it) as Change }

        val removeCard = intentions.removeCard()
                .map { Change.RemoveCard(it) as Change }

        val editName = intentions.editDeckName()
                .map { Change.EditName(it) as Change }

        val editDescription = intentions.editDeckDescription()
                .map { Change.EditDescription(it) as Change }

        val saveDeck = intentions.saveDeck()
                .flatMap { saveDeck() }

        val merged = addCard
                .mergeWith(removeCard)
                .mergeWith(editName)
                .mergeWith(editDescription)
                .mergeWith(saveDeck)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)
    }


    private fun saveDeck(): Observable<Change> {
        val cards = ui.state.pokemonCards
                .plus(ui.state.trainerCards)
                .plus(ui.state.energyCards)
        return if (ui.state.deck == null) {
            repository.createDeck(cards, ui.state.name ?: "", ui.state.description)
                    .map { Change.DeckUpdated(it) as Change }
        }
        else {
            repository.updateDeck(ui.state.deck!!.id, cards, ui.state.name ?: "", ui.state.description)
                    .map { Change.DeckUpdated(it) as Change }
        }
    }
}