package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State.Change
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class DeckBuilderPresenter @Inject constructor(
    ui: DeckBuilderUi,
    val intentions: DeckBuilderUi.Intentions,
    val editor: EditRepository,
    val deckRepository: DeckRepository,
    val collectionRepository: CollectionRepository,
    val marketplaceRepository: MarketplaceRepository,
    val validator: DeckValidator
) : UiPresenter<State, Change>(ui) {

    @SuppressLint("RxSubscribeOnError")
    override fun smashObservables(): Observable<Change> {
        subscribeDirectIntentions()

        val deckUpdates = editor.observeSession(ui.state.deckId)
            .flatMap { deck ->
                val validation = validator.validate(deck.cards)
                    .map { Change.Validated(it) as Change }
                    .startWith(Change.DeckUpdated(deck) as Change)
                    .onErrorReturn(handleUnknownError)

                // We don't want to load pricing information for invalid decks
                val marketplace = if (false) {
                    val cardIds = deck.cards.map { it.id }.toSet()
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

        return deckUpdates
            .mergeWith(observeCollection)
            .mergeWith(editDeck)
            .mergeWith(editOverview)
    }

    private fun subscribeDirectIntentions() {

        val onNext = Consumer<String> {
            Timber.i("Edit complete! Deck($it)")
        }

        disposables += intentions.addCards()
            .flatMap { editor.submit(ui.state.deckId, Edit.AddCards(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, Consumer {
                Timber.e(it, "Error adding cards to deck")
            })

        disposables += intentions.removeCard()
            .flatMap { editor.submit(ui.state.deckId, Edit.RemoveCard(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, Consumer {
                Timber.e(it, "Error removing card from session")
            })

        disposables += intentions.editDeckName()
            .flatMap { editor.submit(ui.state.deckId, Edit.Name(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, Consumer {
                Timber.e(it, "Error changing deck name")
            })

        disposables += intentions.editDeckDescription()
            .flatMap { editor.submit(ui.state.deckId, Edit.Description(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, Consumer {
                Timber.e(it, "Error changing description name")
            })

        disposables += intentions.editDeckCollectionOnly()
            .flatMap { editor.submit(ui.state.deckId, Edit.CollectionOnly(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext, Consumer {
                Timber.e(it, "Error changing collection only")
            })
    }

    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error processing deck")
            Change.Error("Error validating your deck")
        }

        private val handleMarketplaceError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error loading marketplace information")
            Change.PriceProducts(emptyMap())
        }
    }
}
