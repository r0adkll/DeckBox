package com.r0adkll.deckbuilder.arch.ui.features.testing


import android.os.Bundle
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi.State
import com.r0adkll.deckbuilder.arch.ui.features.testing.di.DeckTestingModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import javax.inject.Inject


class DeckTestingActivity : BaseActivity(), DeckTestingUi, DeckTestingUi.Intentions, DeckTestingUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: DeckTestingRenderer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_testing)


        renderer.start()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(DeckTestingModule(this))
                .inject(this)
    }


    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }


    override fun showLoading(isLoading: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showError(description: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun hideError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}