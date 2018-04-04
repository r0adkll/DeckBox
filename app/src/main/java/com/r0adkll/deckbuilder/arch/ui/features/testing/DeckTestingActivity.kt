package com.r0adkll.deckbuilder.arch.ui.features.testing


import android.os.Bundle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.PresenterActivityDelegate
import com.ftinc.kit.arch.presentation.delegates.RendererActivityDelegate
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.testing.TestResults
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi.State
import com.r0adkll.deckbuilder.arch.ui.features.testing.di.DeckTestingModule
import io.reactivex.Observable
import javax.inject.Inject


class DeckTestingActivity : BaseActivity(), DeckTestingUi, DeckTestingUi.Intentions, DeckTestingUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: DeckTestingRenderer
    @Inject lateinit var presenter: DeckTestingPresenter

    private val runTestsRelay: Relay<Int> = PublishRelay.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_testing)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }
    }


    override fun setupComponent() {
        DeckApp.component.plus(DeckTestingModule(this))
                .inject(this)

        addDelegate(RendererActivityDelegate(renderer))
        addDelegate(PresenterActivityDelegate(presenter))
    }


    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }


    override fun runTests(): Observable<Int> {
        return runTestsRelay
    }


    override fun showTestResults(results: TestResults) {

    }


    override fun showLoading(isLoading: Boolean) {

    }


    override fun showError(description: String) {

    }


    override fun hideError() {

    }
}