package com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament

import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import java.util.*


interface TournamentExportUi : StateRenderer<TournamentExportUi.State> {

    val state: State


    interface Intentions {

        fun playerNameChanged(): Observable<String>
        fun playerIdChanged(): Observable<String>
        fun dateOfBirthChanged(): Observable<Date>
        fun ageDivisionChanged(): Observable<AgeDivision>
        fun formatChanged(): Observable<Format>
    }


    interface Actions {

        fun setPlayerName(name: String)
        fun setPlayerId(id: String)
        fun setDateOfBirth(dob: String)
        fun setAgeDivision(ageDivision: AgeDivision)
        fun setFormat(format: Format)
    }


    enum class AgeDivision {
        JUNIOR,
        SENIOR,
        MASTERS
    }


    enum class Format {
        STANDARD,
        EXPANDED
    }


    @PaperParcel
    data class State(
            val playerName: String?,
            val playerID: String?,
            val dob: Date?,
            val ageDivision: AgeDivision?,
            val format: Format?
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            is Change.PlayerName -> this.copy(playerName = change.name)
            is Change.PlayerId -> this.copy(playerID = change.id)
            is Change.DateOfBirth -> this.copy(dob = change.dob)
            is Change.AgeDivisionChange -> this.copy(ageDivision = change.ageDivision)
            is Change.FormatChange -> this.copy(format = change.format)
        }


        sealed class Change(val logText: String) {
            class PlayerName(val name: String) : Change("user -> player name changed $name")
            class PlayerId(val id: String) : Change("user -> player id changed $id")
            class DateOfBirth(val dob: Date) : Change("user -> dob updated $dob")
            class AgeDivisionChange(val ageDivision: AgeDivision) : Change("user -> age division $ageDivision")
            class FormatChange(val format: Format) : Change("user -> Format $format")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelTournamentExportUi_State.CREATOR

            val DEFAULT by lazy {
                State(null, null, null, null, null)
            }
        }
    }
}