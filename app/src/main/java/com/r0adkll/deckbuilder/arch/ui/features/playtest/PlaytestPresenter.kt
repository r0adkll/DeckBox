package com.r0adkll.deckbuilder.arch.ui.features.playtest

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import io.reactivex.Observable
import javax.inject.Inject


class PlaytestPresenter @Inject constructor(
        ui: PlaytestUi,
        val intentions: PlaytestUi.Intentions
) : UiPresenter<PlaytestUi.State, PlaytestUi.State.Change>(ui) {

    override fun smashObservables(): Observable<PlaytestUi.State.Change> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}