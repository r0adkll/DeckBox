package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.data.features.validation.model.SizeRule
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State.Change
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class DeckBuilderPresenter @Inject constructor(
    ui: DeckBuilderUi,
    val intentions: DeckBuilderUi.Intentions,
    val repository: EditRepository,
    val collectionRepository: CollectionRepository,
    val marketplaceRepository: MarketplaceRepository,
    val validator: DeckValidator
) : UiPresenter<State, Change>(ui) {

    @SuppressLint("RxSubscribeOnError")
    override fun smashObservables(): Observable<Change> {
        subscribeDirectIntentions()

        val observeSession = repository.observeSession(ui.state.sessionId)
            .flatMap { session ->
                val validation = validator.validate(session.cards)
                    .map { Change.Validated(it) as Change }
                    .startWith(Change.SessionUpdated(session) as Change)
                    .onErrorReturn(handleUnknownError)

                // We don't want to load pricing information for invalid decks
                val marketplace = if (session.cards.size <= SizeRule.MAX_SIZE) {
                    val cardIds = session.cards.map { it.id }.toSet()
                    marketplaceRepository.getPrices(cardIds)
                        .map { Change.PriceProducts(it) as Change }
                        .onErrorReturn(handleMarketplaceError)
                } else {
                    Observable.empty()
                }

                Observable.merge(validation, marketplace)
            }

        val observeCollection = collectionRepository.observeAll()
            .map { Change.CollectionCounts(it) as Change }
            .onErrorReturn(handleUnknownError)

        val editDeck = intentions.editDeckClicks()
            .map { Change.Editing(it) as Change }

        val editOverview = intentions.editOverviewClicks()
            .map { Change.Overview(it) as Change }

        val saveDeck = intentions.saveDeck()
            .flatMap {
                repository.persistSession(ui.state.sessionId)
                    .map { Change.Saved as Change }
                    .startWith(Change.Saving as Change)
                    .onErrorReturn(handlePersistError)
            }

        return observeSession
            .mergeWith(observeCollection)
            .mergeWith(editDeck)
            .mergeWith(editOverview)
            .mergeWith(saveDeck)
    }

    private fun subscribeDirectIntentions() {
        disposables += intentions.addCards()
            .flatMap { repository.addCards(ui.state.sessionId, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Card(s) added to session")
            }, { t -> Timber.e(t, "Error adding card to session") })

        disposables += intentions.removeCard()
            .flatMap { repository.removeCard(ui.state.sessionId, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Card removed from session")
            }, { t -> Timber.e(t, "Error removing card from session") })

        disposables += intentions.editDeckName()
            .flatMap { repository.changeName(ui.state.sessionId, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Name changed!")
            }, { t -> Timber.e(t, "Error changing deck name") })

        disposables += intentions.editDeckDescription()
            .flatMap { repository.changeDescription(ui.state.sessionId, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Description changed!")
            }, { t -> Timber.e(t, "Error changing description name") })

        disposables += intentions.editDeckCollectionOnly()
            .flatMap { repository.changeCollectionOnly(ui.state.sessionId, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Collection Only changed!")
            }, { t -> Timber.e(t, "Error changing collection only") })
    }

    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error processing deck")
            Change.Error("Error validating your deck")
        }

        private val handlePersistError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error saving deck")
            Change.Error("Error saving your deck")
        }

        private val handleMarketplaceError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error loading marketplace information")
            Change.PriceProducts(emptyMap())
        }
    }
}
