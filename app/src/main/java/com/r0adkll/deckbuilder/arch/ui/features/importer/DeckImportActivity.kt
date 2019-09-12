package com.r0adkll.deckbuilder.arch.ui.features.importer

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.gone
import com.ftinc.kit.kotlin.extensions.setVisible
import com.ftinc.kit.kotlin.extensions.visible
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.features.importer.parser.LineValidator
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.importer.di.DeckImportModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.extensions.service
import com.r0adkll.deckbuilder.util.extensions.systemService
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_importer.*
import javax.inject.Inject


class DeckImportActivity : BaseActivity(), DeckImportUi, DeckImportUi.Intentions, DeckImportUi.Actions {

    override var state: DeckImportUi.State = DeckImportUi.State.DEFAULT

    @Inject lateinit var renderer: DeckImportRenderer
    @Inject lateinit var presenter: DeckImportPresenter

    private val importLineValidator = LineValidator()
    private val clipboardManager: ClipboardManager by service(Context.CLIPBOARD_SERVICE)
    private val importDeck: Relay<String> = PublishRelay.create()

    private var clipboardSnackBar: Snackbar? = null


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

        deckList.hint = HtmlCompat.fromHtml(getString(R.string.hint_import_field), HtmlCompat.FROM_HTML_MODE_LEGACY)

        renderer.start()
        presenter.start()
    }


    override fun onResume() {
        super.onResume()
        detectClipBoard()
    }


    override fun onDestroy() {
        clipboardSnackBar?.dismiss()
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


    private fun detectClipBoard() {
        if (clipboardManager.hasPrimaryClip()) {
            clipboardManager.primaryClip?.let { clip ->
                if (clip.itemCount > 0) {
                    val lines = clip.getItemAt(0).text?.split("\n")
                    if (lines?.any { importLineValidator.validate(it) != null } == true) {
                        // Some valid deck format found, suggest paste
                        if (clipboardSnackBar?.isShownOrQueued == false) {
                            clipboardSnackBar?.setText(R.string.import_clipboard_found_message)
                            clipboardSnackBar?.setActionTextColor(color(R.color.primaryColor))
                            clipboardSnackBar?.setAction(R.string.action_paste) {
                                deckList.setText(clip.getItemAt(0).text)
                            }
                        } else {
                            clipboardSnackBar = Snackbar.make(deckList, R.string.import_clipboard_found_message, Snackbar.LENGTH_INDEFINITE)
                            clipboardSnackBar?.setActionTextColor(color(R.color.primaryColor))
                            clipboardSnackBar?.setAction(R.string.action_paste) {
                                deckList.setText(clip.getItemAt(0).text)
                            }
                            clipboardSnackBar?.show()
                        }
                    }
                }
            }
        }
    }


    companion object {
        private const val RC_IMPORT = 200
        private const val KEY_RESULTS = "DeckImportActivity.Results"

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
