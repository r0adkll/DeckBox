package com.r0adkll.deckbuilder.arch.ui.features.playtest

import android.os.Bundle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.features.playtest.di.PlaytestModule
import com.r0adkll.deckbuilder.util.PresenterActivityDelegate
import com.r0adkll.deckbuilder.util.RendererActivityDelegate
import javax.inject.Inject


class PlaytestActivity : BaseActivity(), PlaytestUi, PlaytestUi.Intentions, PlaytestUi.Actions {

    override var state: PlaytestUi.State = PlaytestUi.State.DEFAULT

    @Inject lateinit var presenter: PlaytestPresenter
    @Inject lateinit var renderer: PlaytestRenderer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playtest_simulator)

    }


    override fun setupComponent() {
        DeckApp.component.plus(PlaytestModule(this))
                .inject(this)
    }


    override fun render(state: PlaytestUi.State) {
        this.state = state
        renderer.render(state)

        addDelegate(RendererActivityDelegate(renderer))
        addDelegate(PresenterActivityDelegate(presenter))
    }


    override fun hideError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showError(description: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showLoading(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}