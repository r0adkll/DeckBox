package com.r0adkll.deckbuilder.arch.ui.features.exporter


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ftinc.kit.util.IntentUtils
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.PTCGOConverter
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.Schedulers
import com.r0adkll.deckbuilder.util.bindParcelable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import kotlinx.android.synthetic.main.activity_deck_exporter.*
import javax.inject.Inject


class DeckExportActivity : BaseActivity() {

    private val deck: Deck by bindParcelable(EXTRA_DECK)

    @Inject lateinit var schedulers: Schedulers
    @Inject lateinit var converter: PTCGOConverter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_exporter)

        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        disposables += converter.export(deck)
                .subscribeOn(schedulers.comp)
                .observeOn(schedulers.main)
                .subscribe { deckList.text = it }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_deck_export, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
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
        @JvmField val EXTRA_DECK = "DeckExportActivity.Deck"

        fun createIntent(context: Context, deck: Deck): Intent {
            val intent = Intent(context, DeckExportActivity::class.java)
            intent.putExtra(EXTRA_DECK, deck)
            return intent
        }
    }
}