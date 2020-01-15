package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.StatefulFragmentDelegate
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.extensions.snackbar
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.textChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ExportTask
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.Format
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportComponent
import com.r0adkll.deckbuilder.arch.ui.features.exporter.preview.PdfPreviewActivity
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.State
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.di.TournamentExportModule
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.AgeDivisionUtils
import com.r0adkll.deckbuilder.util.extensions.setDate
import com.r0adkll.deckbuilder.util.extensions.toCalendar
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tournament_export.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import com.evernote.android.state.State as SaveState

class TournamentExportFragment : BaseFragment(),
    TournamentExportUi,
    TournamentExportUi.Intentions,
    TournamentExportUi.Actions {

    @SaveState override var state: State = State.DEFAULT

    @Inject lateinit var task: ExportTask
    @Inject lateinit var renderer: TournamentExportRenderer
    @Inject lateinit var presenter: TournamentExportPresenter
    @Inject lateinit var exporter: TournamentExporter
    @Inject lateinit var preferences: AppPreferences

    private val dateOfBirthChanges: Relay<Date> = PublishRelay.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tournament_export, container, false)
    }

    @SuppressLint("RxSubscribeOnError")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parent.requestFocus()

        // Prepopulate state
        state = state.copy(
            playerName = preferences.playerName.get(),
            playerId = preferences.playerId.get(),
            dob = preferences.playerDOB.get(),
            ageDivision = preferences.playerAgeDivision.get()
        )

        disposables += inputDateOfBirthLayout.clicks()
            .subscribe {
                val currentDob = state.dob ?: Date()
                val cal = currentDob.toCalendar()

                DatePickerDialog(activity!!, { _, year, month, dayOfMonth ->
                    val pickedDate = Calendar.getInstance().setDate(year, month, dayOfMonth)
                    dateOfBirthChanges.accept(pickedDate.time)
                }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]).show()
            }

        disposables += actionExport.clicks()
            .subscribe { _ ->
                Analytics.event(Event.SelectContent.Action("tournament_export"))
                val playerInfo = state.toPlayerInfo()

                disposables += exporter.export(activity!!, task.deckId, playerInfo)
                    .subscribe({
                        val intent = PdfPreviewActivity.createIntent(activity!!, it)
                        startActivity(intent)
                    }, { t ->
                        Timber.e(t)
                        snackbar("Error exporting your deck")
                    })
            }

        optionAgeDivisionJunior.text = AgeDivisionUtils.divisionLabel(requireContext(), AgeDivision.JUNIOR)
        optionAgeDivisionSenior.text = AgeDivisionUtils.divisionLabel(requireContext(), AgeDivision.SENIOR)
        optionAgeDivisionMasters.text = AgeDivisionUtils.divisionLabel(requireContext(), AgeDivision.MASTERS)
    }

    override fun setupComponent() {
        getComponent(MultiExportComponent::class)
            .plus(TournamentExportModule(this))
            .inject(this)

        delegates += StatefulFragmentDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulFragmentDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun playerNameChanged(): Observable<String> {
        return inputPlayerName.textChanges()
            .map { it.toString() }
            .uiDebounce()
    }

    override fun playerIdChanged(): Observable<String> {
        return inputPlayerId.textChanges()
            .map { it.toString() }
            .uiDebounce()
    }

    override fun dateOfBirthChanged(): Observable<Date> {
        return dateOfBirthChanges
    }

    override fun ageDivisionChanged(): Observable<AgeDivision> {
        return optionsAgeDivision.checkedChanges()
            .map {
                when (it) {
                    R.id.optionAgeDivisionJunior -> AgeDivision.JUNIOR
                    R.id.optionAgeDivisionSenior -> AgeDivision.SENIOR
                    else -> AgeDivision.MASTERS
                }
            }
    }

    override fun formatChanged(): Observable<Format> {
        return optionsFormat.checkedChanges().skipInitialValue()
            .map {
                when (it) {
                    R.id.optionFormatStandard -> Format.STANDARD
                    else -> Format.EXPANDED
                }
            }
    }

    override fun setPlayerName(name: String?) {
        if (inputPlayerName.text.toString() != name) {
            inputPlayerName.setText(name)
        }
    }

    override fun setPlayerId(id: String?) {
        if (inputPlayerId.text.toString() != id) {
            inputPlayerId.setText(id)
        }
    }

    override fun setDateOfBirth(dob: String?) {
        inputDateOfBirth.text = dob
    }

    override fun setAgeDivision(ageDivision: AgeDivision?) {
        optionsAgeDivision.check(when (ageDivision) {
            AgeDivision.JUNIOR -> R.id.optionAgeDivisionJunior
            AgeDivision.SENIOR -> R.id.optionAgeDivisionSenior
            AgeDivision.MASTERS -> R.id.optionAgeDivisionMasters
            null -> -1
        })
    }

    override fun setFormat(format: Format?) {
        optionsFormat.check(when (format) {
            Format.STANDARD -> R.id.optionFormatStandard
            Format.EXPANDED -> R.id.optionFormatExpanded
            null -> -1
        })
    }

    companion object {

        fun newInstance(): TournamentExportFragment = TournamentExportFragment()
    }
}
