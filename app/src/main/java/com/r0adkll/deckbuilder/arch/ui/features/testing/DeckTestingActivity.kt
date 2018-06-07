package com.r0adkll.deckbuilder.arch.ui.features.testing


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.utils.bindLong
import com.ftinc.kit.kotlin.utils.bindOptionalString
import com.ftinc.kit.widget.DividerSpacerItemDecoration
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.longClicks
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi.State
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResult
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.testing.di.DeckTestingModule
import com.r0adkll.deckbuilder.util.PresenterActivityDelegate
import com.r0adkll.deckbuilder.util.RendererActivityDelegate
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_testing.*
import javax.inject.Inject


class DeckTestingActivity : BaseActivity(), DeckTestingUi, DeckTestingUi.Intentions, DeckTestingUi.Actions {

    private val sessionId: Long by bindLong(EXTRA_SESSION_ID, -1L)
    private val deckId: String? by bindOptionalString(EXTRA_DECK_ID)

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: DeckTestingRenderer
    @Inject lateinit var presenter: DeckTestingPresenter

    private lateinit var adapter: TestResultsRecyclerAdapter


    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_testing)

        // Configure state
        if (sessionId != -1L) {
            state = state.copy(sessionId = sessionId)
        } else if (deckId != null) {
            state = state.copy(deckId = deckId)
        }

        // Setup appbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }

        // Setup result recycler
        adapter = TestResultsRecyclerAdapter(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerSpacerItemDecoration(dpToPx(8f), false))
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
        return actionTest.clicks()
                .uiDebounce()
                .map { state.iterations }
    }


    override fun incrementIterations(): Observable<Int> {
        val incrementSmall = actionIterationPlus.clicks()
                .map { STEP }

        val incrementLarge = actionIterationPlus.longClicks()
                .map { LARGE_STEP }

        return incrementSmall.mergeWith(incrementLarge)
    }


    override fun decrementIterations(): Observable<Int> {
        val decrementSmall = actionIterationMinus.clicks()
                .map { STEP }

        val decrementLarge = actionIterationMinus.longClicks()
                .map { LARGE_STEP }

        return decrementSmall.mergeWith(decrementLarge)
    }


    override fun setMetadata(metadata: DeckTestingUi.Metadata) {
        name.text = metadata.name
        description.text = metadata.description
        cardCount.count(metadata.pokemon, metadata.trainer, metadata.energy)
    }


    override fun showTestResults(results: List<TestResult>) {
        adapter.setTestResults(results)
    }


    override fun setTestIterations(iterations: Int) {
        inputIterations.setText("$iterations")
    }


    override fun showLoading(isLoading: Boolean) {

    }


    override fun showError(description: String) {

    }


    override fun hideError() {

    }


    companion object {
        private const val EXTRA_SESSION_ID = "DeckTestingActivity.SessionId"
        private const val EXTRA_DECK_ID = "DeckTestingActivity.DeckId"
        private const val STEP = 100
        private const val LARGE_STEP = 1000


        fun createIntent(context: Context, sessionId: Long): Intent {
            val intent = Intent(context, DeckTestingActivity::class.java)
            intent.putExtra(EXTRA_SESSION_ID, sessionId)
            return intent
        }


        fun createIntent(context: Context, deckId: String): Intent {
            val intent = Intent(context, DeckTestingActivity::class.java)
            intent.putExtra(EXTRA_DECK_ID, deckId)
            return intent
        }
    }
}