package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament


import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.ExportTask
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.Format
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State.*
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class TournamentExportPresenter @Inject constructor(
        val exportTask: ExportTask,
        val ui: TournamentExportUi,
        val intentions: TournamentExportUi.Intentions,
        val preferences: AppPreferences,
        val validator: DeckValidator
) : Presenter() {

    override fun start() {

        val initialFormat = when {
            exportTask.sessionId != null -> validator.validate(exportTask.sessionId)
            exportTask.deckId != null -> validator.validate(exportTask.deckId)
            else -> Observable.empty()
        }.map { when {
            it.standard -> Format.STANDARD
            it.expanded -> Format.EXPANDED
            else -> Format.EXPANDED
        } }.map { Change.FormatChange(it) as Change }

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


        val merged = initialFormat
                .mergeWith(nameChange)
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