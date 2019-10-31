package com.r0adkll.deckbuilder.arch.ui.features.exporter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ftinc.kit.util.IntentUtils
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ptcgo.PtcgoExporter
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.bindParcelable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.snackbar
import com.r0adkll.deckbuilder.util.extensions.toast
import kotlinx.android.synthetic.main.activity_deck_exporter.*
import timber.log.Timber
import javax.inject.Inject

class DeckExportActivity : BaseActivity() {

    private val deck: Deck by bindParcelable(EXTRA_DECK)

    private val clipboard: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Inject lateinit var schedulers: AppSchedulers
    @Inject lateinit var exporter: PtcgoExporter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_exporter)

        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        actionCopy?.setOnClickListener {
            Analytics.event(Event.SelectContent.Action("copy_decklist"))
            val text = deckList.text.toString()
            val clip = ClipData.newPlainText(deck.name, text)
            clipboard.setPrimaryClip(clip)
            toast(getString(R.string.deck_copied_format, deck.name))
        }

        actionShare?.setOnClickListener {
            Analytics.event(Event.Share("deck"))
            val text = deckList.text.toString()
            val intent = Intent.createChooser(IntentUtils.shareText(null, text), "Share deck")
            startActivity(intent)
        }

        disposables += exporter.export(deck.cards, deck.name)
                .subscribeOn(schedulers.comp)
                .observeOn(schedulers.main)
                .subscribe({
                    deckList.text = it
                }, {
                    Timber.e(it)
                    snackbar(R.string.error_exporting_deck)
                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_deck_export, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
                Analytics.event(Event.Share("deck"))
                val text = deckList.text.toString()
                val intent = Intent.createChooser(IntentUtils.shareText(null, text), "Share deck")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupComponent(component: AppComponent) {
        component.inject(this)
    }

    companion object {
        private const val EXTRA_DECK = "DeckExportActivity.Deck"

        fun createIntent(context: Context, deck: Deck): Intent {
            val intent = Intent(context, DeckExportActivity::class.java)
            intent.putExtra(EXTRA_DECK, deck)
            return intent
        }
    }
}
