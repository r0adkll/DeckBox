package com.r0adkll.deckbuilder.arch.ui.features.missingcards


import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.model.MissingCard
import com.r0adkll.deckbuilder.arch.domain.features.missingcard.repository.MissingCardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsUi.State
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsUi.State.*
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class MissingCardsPresenter @Inject constructor(
        val ui: MissingCardsUi,
        val intentions: MissingCardsUi.Intentions,
        val repository: CardRepository,
        val missingCardRepository: MissingCardRepository
) : Presenter() {

    override fun start() {

        val loadExpansions = repository.getExpansions()
                .map { Change.ExpansionsLoaded(it) as Change }
                .onErrorReturn(handleUnknownError)

        val editName = intentions.editName()
                .map { Change.EditName(it) as Change }

        val editNumber = intentions.editNumber()
                .map { Change.EditNumber(it) as Change }

        val editDescription = intentions.editDescription()
                .map { Change.EditDescription(it) as Change }

        val selectExpansion = intentions.selectExpansion()
                .map { Change.SelectedExpansion(it) as Change }

        val selectPrint = intentions.selectPrint()
                .map { Change.SelectedPrint(it) as Change }

        val submitReport = intentions.submitReport()
                .flatMap {
                    val missingCard = MissingCard(ui.state.name!!,
                            ui.state.setNumber,
                            ui.state.description,
                            ui.state.expansion?.code,
                            ui.state.print)
                    missingCardRepository.reportMissingCard(missingCard)
                            .map { Change.ReportSubmitted }
                }

        val merged = loadExpansions
                .mergeWith(editName)
                .mergeWith(editNumber)
                .mergeWith(editDescription)
                .mergeWith(selectExpansion)
                .mergeWith(selectPrint)
                .mergeWith(submitReport)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }

    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error during missing card report")
            Change.Error(it.localizedMessage ?: "Something went wrong")
        }
    }
}