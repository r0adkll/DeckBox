package com.r0adkll.deckbuilder.arch.ui.features.importer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.ftinc.kit.kotlin.extensions.gone
import com.ftinc.kit.kotlin.extensions.setVisible
import com.ftinc.kit.kotlin.extensions.visible
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.importer.di.DeckImportModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_importer.*
import javax.inject.Inject


class DeckImportActivity : BaseActivity(), DeckImportUi, DeckImportUi.Intentions, DeckImportUi.Actions {

    override var state: DeckImportUi.State = DeckImportUi.State.DEFAULT

    @Inject lateinit var renderer: DeckImportRenderer
    @Inject lateinit var presenter: DeckImportPresenter

    private val importDeck: Relay<String> = PublishRelay.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_importer)

        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        actionImport?.setOnClickListener {
            val text = deckList.text.toString().trim()
            if (text.isNotBlank()) {
                importDeck.accept(text)
            }
        }

        deckList.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                invalidateOptionsMenu()
                action_layout?.setVisible(deckList.text.isNotBlank())
                action_divider?.setVisible(deckList.text.isNotBlank())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        renderer.start()
        presenter.start()
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_deck_import, menu)
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val importItem = menu.findItem(R.id.action_import)
        importItem?.isVisible = deckList.text.isNotBlank()
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_import -> {
                val text = deckList.text.toString().trim()
                if (text.isNotBlank()) {
                    importDeck.accept(text)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(DeckImportModule(this))
                .inject(this)
    }


    override fun importDeckList(): Observable<String> {
        return importDeck
    }


    override fun render(state: DeckImportUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun setResults(cards: List<PokemonCard>) {
        val data = Intent()
        data.putParcelableArrayListExtra(KEY_RESULTS, ArrayList(cards))
        setResult(RESULT_OK, data)

        if(cards.isNotEmpty()) {
            supportFinishAfterTransition()
        }
    }


    override fun showLoading(isLoading: Boolean) {
        loading.setVisible(isLoading)
        deckList.setVisible(!isLoading)
    }


    override fun showError(description: String) {
        errorMessage.text = description
        error.visible()
    }


    override fun hideError() {
        error.gone()
    }


    companion object {
        @JvmField val RC_IMPORT = 200
        @JvmField val KEY_RESULTS = "DeckImportActivity.Results"


        fun show(activity: Activity) {
            val intent = Intent(activity, DeckImportActivity::class.java)
            activity.startActivityForResult(intent, RC_IMPORT)
        }


        fun parseResults(resultCode: Int, requestCode: Int, data: Intent?): List<PokemonCard>? {
            if (resultCode == Activity.RESULT_OK && requestCode == RC_IMPORT) {
                return data?.getParcelableArrayListExtra(KEY_RESULTS)
            }
            return null
        }
    }
}