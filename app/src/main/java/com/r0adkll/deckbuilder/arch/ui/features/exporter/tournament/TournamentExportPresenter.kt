package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ExportTask
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.Format
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State.Change
import io.reactivex.Observable
import javax.inject.Inject

class TournamentExportPresenter @Inject constructor(
    ui: TournamentExportUi,
    val task: ExportTask,
    val intentions: TournamentExportUi.Intentions,
    val preferences: AppPreferences,
    val validator: DeckValidator
) : UiPresenter<State, Change>(ui) {

    @SuppressLint("RxSubscribeOnError")
    override fun smashObservables(): Observable<Change> {

        val initialFormat = validator.validate(task.deckId).map {
            when {
                it.standard -> Format.STANDARD
                it.expanded -> Format.EXPANDED
                else -> Format.EXPANDED
            }
        }.map { Change.FormatChange(it) as Change }

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

        /* Intentions directly change preferences */

        disposables += intentions.playerNameChanged()
            .subscribe(preferences.playerName.asConsumer())

        disposables += intentions.playerIdChanged()
            .subscribe(preferences.playerId.asConsumer())

        disposables += intentions.dateOfBirthChanged()
            .subscribe(preferences.playerDOB.asConsumer())

        disposables += intentions.ageDivisionChanged()
            .subscribe(preferences.playerAgeDivision.asConsumer())

        return initialFormat
            .mergeWith(nameChange)
            .mergeWith(idChange)
            .mergeWith(dobChange)
            .mergeWith(ageChange)
            .mergeWith(formatChange)
    }
}
