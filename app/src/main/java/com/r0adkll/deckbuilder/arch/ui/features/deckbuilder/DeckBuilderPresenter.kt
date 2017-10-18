package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State.*
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class DeckBuilderPresenter @Inject constructor(
        val ui: DeckBuilderUi,
        val intentions: DeckBuilderUi.Intentions
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

        val merged = addCard
                .mergeWith(removeCard)
                .mergeWith(editName)
                .mergeWith(editDescription)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)
    }
}