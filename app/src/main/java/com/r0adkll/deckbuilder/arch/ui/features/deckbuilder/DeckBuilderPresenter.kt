package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import javax.inject.Inject


class DeckBuilderPresenter @Inject constructor(
        val ui: DeckBuilderUi,
        val intentions: DeckBuilderUi.Intentions
) : Presenter() {

    override fun start() {



    }
}