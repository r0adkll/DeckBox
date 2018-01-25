package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament


import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State.*
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class TournamentExportPresenter @Inject constructor(
        val deck: Deck,
        val ui: TournamentExportUi,
        val intentions: TournamentExportUi.Intentions
) : Presenter() {

    override fun start() {

        val nameChange = intentions.playerNameChanged()
                .map { Change.PlayerName(it) as Change }

        val idChange = intentions.playerIdChanged()
                .map { Change.PlayerId(it) as Change }

        val dobChange = intentions.dateOfBirthChanged()
                .map { Change.DateOfBirth(it) as Change }

        val ageChange = intentions.ageDivisionChanged()
                .map { Change.AgeDivisionChange(it) as Change }

        val formatChange = intentions.formatChanged()
                .map { Change.FormatChange(it) as Change }

        val merged = nameChange
                .mergeWith(idChange)
                .mergeWith(dobChange)
                .mergeWith(ageChange)
                .mergeWith(formatChange)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }
    }
}