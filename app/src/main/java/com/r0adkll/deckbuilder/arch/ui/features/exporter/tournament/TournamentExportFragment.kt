package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.*
import com.ftinc.kit.util.IntentUtils
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.textChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.tournament.exporter.TournamentExporter
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.Format
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.PlayerInfo
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportComponent
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportUi.*
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.di.TournamentExportModule
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_tournament_export.*
import java.util.*
import javax.inject.Inject


class TournamentExportFragment : BaseFragment(), TournamentExportUi, TournamentExportUi.Intentions,
        TournamentExportUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var deck: Deck
    @Inject lateinit var renderer: TournamentExportRenderer
    @Inject lateinit var presenter: TournamentExportPresenter
    @Inject lateinit var exporter: TournamentExporter

    private val dateOfBirthChanges: Relay<Date> = PublishRelay.create()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tournament_export, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        parent.requestFocus()

        renderer.start()
        presenter.start()
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.activity_export_tournament, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_export -> {
                val playerInfo = state.toPlayerInfo()
                disposables += exporter.export(activity!!, deck, playerInfo)
                        .subscribe {
                            val uri = FileProvider.getUriForFile(activity!!, BuildConfig.APPLICATION_ID, it)
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(uri, "application/pdf")
                            startActivity(intent)
                        }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun setupComponent() {
        getComponent(MultiExportComponent::class)
                .plus(TournamentExportModule(this))
                .inject(this)
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
                .map { when(it) {
                    R.id.optionAgeDivisionJunior -> AgeDivision.JUNIOR
                    R.id.optionAgeDivisionSenior -> AgeDivision.SENIOR
                    else -> AgeDivision.MASTERS
                } }
    }


    override fun formatChanged(): Observable<Format> {
        return optionsFormat.checkedChanges()
                .map { when(it) {
                    R.id.optionFormatStandard -> Format.STANDARD
                    else -> Format.EXPANDED
                } }
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
        inputDateOfBirth.setText(dob)
    }


    override fun setAgeDivision(ageDivision: AgeDivision?) {
        optionsAgeDivision.check(when(ageDivision) {
            AgeDivision.JUNIOR -> R.id.optionAgeDivisionJunior
            AgeDivision.SENIOR -> R.id.optionAgeDivisionSenior
            AgeDivision.MASTERS -> R.id.optionAgeDivisionMasters
            null -> -1
        })
    }


    override fun setFormat(format: Format?) {
        optionsFormat.check(when(format) {
            Format.STANDARD -> R.id.optionFormatStandard
            Format.EXPANDED -> R.id.optionFormatExpanded
            null -> -1
        })
    }


    companion object {

        fun newInstance(): TournamentExportFragment = TournamentExportFragment()
    }
}