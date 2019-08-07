package com.r0adkll.deckbuilder.arch.ui.features.playtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.arch.ui.features.playtest.di.PlaytestModule
import com.r0adkll.deckbuilder.arch.ui.widgets.BoardView
import com.r0adkll.deckbuilder.util.extensions.toast
import kotlinx.android.synthetic.main.activity_playtest_simulator.*
import javax.inject.Inject


class PlaytestActivity : BaseActivity(), PlaytestUi, PlaytestUi.Intentions, PlaytestUi.Actions {

    override var state: PlaytestUi.State = PlaytestUi.State.DEFAULT

    @Inject lateinit var presenter: PlaytestPresenter
    @Inject lateinit var renderer: PlaytestRenderer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playtest_simulator)

        playmat.setBoardListener(object : BoardView.BoardListener {
            override fun onBoardElementClicked(playerType: Board.Player.Type, elementType: BoardView.BoardElement, element: BoardView.Element) {
                toast("Element Clicked ($playerType, $elementType, $element)")
            }

            override fun onBoardElementLongClicked(playerType: Board.Player.Type, elementType: BoardView.BoardElement, element: BoardView.Element) {
                toast("Element Long Clicked ($playerType, $elementType, $element)")
            }
        })
    }


    override fun setupComponent() {
        DeckApp.component.plus(PlaytestModule(this))
                .inject(this)
    }


    override fun render(state: PlaytestUi.State) {
        this.state = state
        renderer.render(state)

//        addDelegate(RendererActivityDelegate(renderer))
//        addDelegate(PresenterActivityDelegate(presenter))
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


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, PlaytestActivity::class.java)
    }
}