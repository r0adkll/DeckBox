package com.r0adkll.deckbuilder.arch.ui.features.testing

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.data.features.testing.InvalidDeckException
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi.State.*
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class DeckTestingPresenter @Inject constructor(
        ui: DeckTestingUi,
        val intentions: DeckTestingUi.Intentions,
        val tester: DeckTester,
        val deckRepository: DeckRepository,
        val editRepository: EditRepository
) : UiPresenter<DeckTestingUi.State, DeckTestingUi.State.Change>(ui) {

    override fun smashObservables(): Observable<DeckTestingUi.State.Change> {

        val loadMetaData = when {
            ui.state.sessionId != null -> editRepository.getSession(ui.state.sessionId!!)
                    .map {
                        DeckTestingUi.Metadata(it.name, it.description,
                                it.cards.count { it.supertype == SuperType.POKEMON },
                                it.cards.count { it.supertype == SuperType.TRAINER },
                                it.cards.count { it.supertype == SuperType.ENERGY })
                    }
            ui.state.deckId != null -> deckRepository.getDeck(ui.state.deckId!!)
                    .map {
                        DeckTestingUi.Metadata(it.name, it.description,
                                it.pokemonCount, it.trainerCount, it.energyCount)
                    }
            else -> Observable.empty()
        }.map {
            Change.MetadataLoaded(it) as Change
        }

        val incrementIterations = intentions.incrementIterations()
                .map { Change.IncrementIterations(it) as Change }

        val decrementIterations = intentions.decrementIterations()
                .map { Change.DecrementIterations(it) as Change }

        val testSingleHand = intentions.testSingleHand()
                .flatMap { iterations ->
                    val testObservable = when {
                        ui.state.sessionId != null -> tester.testHand(ui.state.sessionId!!, iterations)
                        ui.state.deckId != null -> tester.testHandById(ui.state.deckId!!, iterations)
                        else -> Observable.empty()
                    }
                    testObservable
                            .map { Change.Hand(it) as Change }
                            .startWith(Change.IsLoading as Change)
                            .onErrorReturn(handleUnknownError)
                }

        val testOverallHands = intentions.testOverallHands()
                .flatMap { iterations ->
                    val testObservable = when {
                        ui.state.sessionId != null -> tester.testSession(ui.state.sessionId!!, iterations)
                        ui.state.deckId != null -> tester.testDeckById(ui.state.deckId!!, iterations)
                        else -> Observable.empty()
                    }

                    testObservable
                            .map { Change.Results(it) as Change }
                            .startWith(Change.IsLoading as Change)
                            .onErrorReturn(handleUnknownError)
                }

        return loadMetaData
                .mergeWith(incrementIterations)
                .mergeWith(decrementIterations)
                .mergeWith(testSingleHand)
                .mergeWith(testOverallHands)
    }


    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error testing deck")
            if (it is InvalidDeckException) {
                Change.Error("This deck is invalid. Please fix before testing.")
            } else {
                Change.Error("Unable to test this deck")
            }
        }
    }
}