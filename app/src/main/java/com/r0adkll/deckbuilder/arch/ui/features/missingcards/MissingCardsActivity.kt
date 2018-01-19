package com.r0adkll.deckbuilder.arch.ui.features.missingcards


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import com.ftinc.kit.kotlin.extensions.setVisible
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.itemSelections
import com.jakewharton.rxbinding2.widget.textChanges
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsUi.State
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.adapter.ExpansionSpinnerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.di.MissingCardsModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.extensions.moveCursorToEnd
import com.r0adkll.deckbuilder.util.extensions.snackbar
import com.r0adkll.deckbuilder.util.extensions.toast
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_missing_cards.*
import javax.inject.Inject


class MissingCardsActivity : BaseActivity(),
        MissingCardsUi, MissingCardsUi.Intentions, MissingCardsUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: MissingCardsRenderer
    @Inject lateinit var presenter: MissingCardsPresenter

    private lateinit var expansionAdapter: ExpansionSpinnerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_missing_cards)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        expansionAdapter = ExpansionSpinnerAdapter(this)
        expansionSpinner.adapter = expansionAdapter
        expansionSpinner.prompt = "Pick expansion"

        renderer.start()
        presenter.start()
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(MissingCardsModule(this))
                .inject(this)
    }


    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }


    override fun editName(): Observable<String> {
        return inputCardName.textChanges()
                .map { it.toString() }
                .uiDebounce()
    }


    override fun editNumber(): Observable<Int> {
        return inputSetNumber.textChanges()
                .map { it.toString().toIntOrNull() ?: -1 }
                .uiDebounce()
    }


    override fun editDescription(): Observable<String> {
        return inputCardDescription.textChanges()
                .map { it.toString() }
                .uiDebounce()
    }


    override fun selectExpansion(): Observable<Expansion> {
        return expansionSpinner.itemSelections()
                .filter { it != AdapterView.INVALID_POSITION }
                .map {
                    expansionAdapter.getItem(it)
                }
    }


    override fun selectPrint(): Observable<String> {
        return printSpinner.itemSelections()
                .filter { it != AdapterView.INVALID_POSITION }
                .map {
                    val values = resources.getStringArray(R.array.print_varieties)
                    values[it]
                }
    }


    override fun submitReport(): Observable<Unit> {
        return actionSend.clicks()
                .uiDebounce()
    }


    override fun setExpansions(expansions: List<Expansion>) {
        expansionAdapter.clear()
        expansionAdapter.addAll(expansions)
        expansionAdapter.notifyDataSetChanged()
    }


    override fun setExpansion(expansion: Expansion?) {
        val index = expansion?.let {
            expansionAdapter.getPosition(expansion)
        } ?: 0
        expansionSpinner.setSelection(index)
    }


    override fun setName(name: String?) {
        if (inputCardName.text.toString() != name) {
            inputCardName.setText(name)
            inputCardName.moveCursorToEnd()
        }
    }


    override fun setNumber(number: Int?) {
        if (inputSetNumber.text.toString() != number?.toString()) {
            inputSetNumber.setText(number?.toString())
            inputSetNumber.moveCursorToEnd()
        }
    }


    override fun setDescription(description: String?) {
        if (inputCardDescription.text.toString() != description) {
            inputCardDescription.setText(description)
            inputCardDescription.moveCursorToEnd()
        }
    }


    override fun setPrint(print: String) {
        val values = resources.getStringArray(R.array.print_varieties)
        val index = values.indexOf(print)
        if (index != -1) {
            printSpinner.setSelection(index)
        }
    }


    override fun setSendEnabled(enabled: Boolean) {
        actionSend.isEnabled = enabled
    }


    override fun closeReport() {
        toast(R.string.missing_card_report_submitted)
        supportFinishAfterTransition()
    }


    override fun showLoading(isLoading: Boolean) {
        loading.setVisible(isLoading)
        if (isLoading) {
            actionSend.isEnabled = false
        } else {
            actionSend.isEnabled = state.isReportReady
        }
    }


    override fun showError(description: String) {
        snackbar(description)
    }


    override fun hideError() {
    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, MissingCardsActivity::class.java)


        fun show(context: Context) {
            context.startActivity(createIntent(context))
        }
    }
}