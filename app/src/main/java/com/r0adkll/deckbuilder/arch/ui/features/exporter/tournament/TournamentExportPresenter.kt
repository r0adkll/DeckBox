package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament


import com.r0adkll.deckbuilder.arch.data.AppPreferences
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
        val intentions: TournamentExportUi.Intentions,
        val preferences: AppPreferences
) : Presenter() {

    override fun start() {

        val formatChange = intentions.formatChanged()
                .map { Change.FormatChange(it) as Change }

        /* Preference Changes */

        val nameChange = preferences.playerName.asObservable()
                .map { Change.PlayerName(it) as Change }

        val idChange = preferences.playerId.asObservable()
                .map { Change.PlayerId(it) as Change }

        val dobChange = preferences.playerDOB.asObservable()
                .map { Change.DateOfBirth(it) as Change }

        val ageChange = preferences.playerAgeDivision.asObservable()
                .map { Change.AgeDivisionChange(it) as Change }


        val merged = nameChange
                .mergeWith(idChange)
                .mergeWith(dobChange)
                .mergeWith(ageChange)
                .mergeWith(formatChange)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }


        /* Intentions directly change preferences */

        disposables += intentions.playerNameChanged()
                .subscribe(preferences.playerName.asConsumer())

        disposables += intentions.playerIdChanged()
                .subscribe(preferences.playerId.asConsumer())

        disposables += intentions.dateOfBirthChanged()
                .subscribe(preferences.playerDOB.asConsumer())

        disposables += intentions.ageDivisionChanged()
                .subscribe(preferences.playerAgeDivision.asConsumer())
    }
}