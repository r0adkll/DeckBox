package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailUi.State.Change
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class CardDetailPresenter @Inject constructor(
    ui: CardDetailUi,
    val intentions: CardDetailUi.Intentions,
    val deckRepository: DeckRepository,
    val cardRepository: CardRepository,
    val collectionRepository: CollectionRepository,
    val marketplaceRepository: MarketplaceRepository,
    val editor: EditRepository,
    val validator: DeckValidator
) : UiPresenter<State, Change>(ui) {

    @Suppress("LongMethod")
    @SuppressLint("RxSubscribeOnError")
    override fun smashObservables(): Observable<Change> {

        val observeSession = ui.state.deckId?.let { deckId ->
            deckRepository.observeDeck(deckId)
                .map { deck ->
                    deck.cards.filter { it.id == ui.state.card?.id }.count()
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

        val loadPrice = marketplaceRepository.getPrice(ui.state.card!!.id)
            .map { Change.PriceUpdated(it) as Change }
            .onErrorReturn(handleUnknownError)

        val loadVariants = cardRepository.search(ui.state.card!!.supertype, ui.state.card!!.name)
            .map { it.filter { it.id != ui.state.card!!.id } }
            .map { Change.VariantsLoaded(it) as Change }
            .onErrorReturn(handleUnknownError)

        val loadEvolves = ui.state.card!!.evolvesFrom?.let {
            cardRepository.search(ui.state.card!!.supertype, it)
                .map { Change.EvolvesFromLoaded(it) as Change }
                .onErrorReturn(handleUnknownError)
        } ?: Observable.empty()

        val loadEvolvesTo = cardRepository.search(ui.state.card!!.supertype, "", Filter(evolvesFrom = ui.state.card!!.name))
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

        val deckId = ui.state.deckId
        if (deckId != null) {
            disposables += intentions.addCardClicks()
                .flatMap { editor.submit(deckId, Edit.AddCards(ui.state.card!!)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Card(s) added to session")
                }, { t -> Timber.e(t, "Error adding card to session") })

            disposables += intentions.removeCardClicks()
                .flatMap { editor.submit(deckId, Edit.RemoveCard(ui.state.card!!)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Card removed from session")
                }, { t -> Timber.e(t, "Error removing card from session") })
        }

        return observeSession
            .mergeWith(loadVariants)
            .mergeWith(loadValidation)
            .mergeWith(loadCollectionCount)
            .mergeWith(loadPrice)
            .mergeWith(loadEvolves)
            .mergeWith(loadEvolvesTo)
            .mergeWith(incrementCollectionCount)
            .mergeWith(decrementCollectionCount)
    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error has occurred")
            Change.Error("An unknown error has occurred")
        }
    }
}
