package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State.Change
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject


class CardDetailPresenter @Inject constructor(
        val ui: CardDetailUi,
        val intentions: CardDetailUi.Intentions,
        val repository: CardRepository,
        val editor: EditRepository,
        val validator: DeckValidator
) : Presenter() {

    override fun start() {

        val observeSession = ui.state.sessionId?.let {
            editor.observeSession(it)
                    .map {
                        it.cards.filter { it.id == ui.state.card?.id }.count()
                    }
                    .map { Change.CountChanged(it) as Change }
        } ?: Observable.empty<Change>()

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

        disposables += intentions.addCardClicks()
                .flatMap { editor.addCards(ui.state.sessionId!!, listOf(ui.state.card!!)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Card(s) added to session")
                }, { t -> Timber.e(t, "Error adding card to session")})

        disposables += intentions.removeCardClicks()
                .flatMap { editor.removeCard(ui.state.sessionId!!, ui.state.card!!) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Card removed from session")
                }, { t -> Timber.e(t, "Error removing card from session")})

        val merged = observeSession
                .mergeWith(loadVariants)
                .mergeWith(loadValidation)
                .mergeWith(loadEvolves)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }
    }
}