package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
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
        val collectionRepository: CollectionRepository,
        val editor: EditRepository,
        val validator: DeckValidator
) : Presenter() {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        val observeSession = ui.state.sessionId?.let {
            editor.observeSession(it)
                    .map {
                        it.cards.filter { it.id == ui.state.card?.id }.count()
                    }
                    .map { Change.CountChanged(it) as Change }
                    .onErrorReturn(handleUnknownError)
        } ?: Observable.empty<Change>()

        val loadValidation = validator.validate(listOf(ui.state.card!!))
                .map { Change.Validated(it) as Change }
                .onErrorReturn(handleUnknownError)

        val loadCollectionCount = collectionRepository.getCount(ui.state.card!!.id)
                .map { Change.CollectionCountChanged(it.count) as Change }
                .onErrorReturn(handleUnknownError)

        val loadVariants = repository.search(ui.state.card!!.supertype, ui.state.card!!.name)
                .map { it.filter { it.id != ui.state.card!!.id } }
                .map { Change.VariantsLoaded(it) as Change }
                .onErrorReturn(handleUnknownError)

        val loadEvolves = ui.state.card!!.evolvesFrom?.let {
            repository.search(ui.state.card!!.supertype, it)
                    .map { Change.EvolvesFromLoaded(it) as Change }
                    .onErrorReturn(handleUnknownError)
        } ?: Observable.empty()

        val loadEvolvesTo = repository.search(ui.state.card!!.supertype, "", Filter(evolvesFrom = ui.state.card!!.name))
                .map { Change.EvolvesToLoaded(it) as Change }
                .onErrorReturn(handleUnknownError)

        val incrementCollectionCount = intentions.incrementCollectionCount()
                .flatMap {
                    collectionRepository.incrementCount(ui.state.card!!)
                            .map { Change.CollectionCountUpdated(0) as Change }
                            .startWith(Change.CollectionCountUpdated(1))
                            .onErrorReturn { Change.CollectionCountUpdated(-1) }
                }

        val decrementCollectionCount = intentions.decrementCollectionCount()
                .flatMap {
                    collectionRepository.decrementCount(ui.state.card!!)
                            .map { Change.CollectionCountUpdated(0) as Change }
                            .startWith(Change.CollectionCountUpdated(-1))
                            .onErrorReturn { Change.CollectionCountUpdated(1) }
                }

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
                .mergeWith(loadCollectionCount)
                .mergeWith(loadEvolves)
                .mergeWith(loadEvolvesTo)
                .mergeWith(incrementCollectionCount)
                .mergeWith(decrementCollectionCount)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }
    }


    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error has occurred")
            Change.Error("An unknown error has occurred")
        }
    }
}