package com.r0adkll.deckbuilder.arch.ui.features.exporter.ptcgo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.ftinc.kit.util.IntentUtils
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.ptcgo.repository.PTCGOConverter
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportComponent
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.Schedulers
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.toast
import kotlinx.android.synthetic.main.activity_deck_exporter.*
import javax.inject.Inject


class PtcgoExportFragment : BaseFragment() {

    private val clipboard: ClipboardManager by lazy {
        activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Inject lateinit var schedulers: Schedulers
    @Inject lateinit var converter: PTCGOConverter
    @Inject lateinit var deck: Deck



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ptcgo_export, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        disposables += converter.export(deck)
                .subscribeOn(schedulers.comp)
                .observeOn(schedulers.main)
                .subscribe { deckList.text = it }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.activity_export_ptcgo, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_copy -> {
                Analytics.event(Event.SelectContent.Action("copy_decklist"))
                val text = deckList.text.toString()
                val clip = ClipData.newPlainText(deck.name, text)
                clipboard.primaryClip = clip
                toast(getString(R.string.deck_copied_format, deck.name))
                true
            }
            R.id.action_export -> {
                Analytics.event(Event.Share("deck"))
                val text = deckList.text.toString()
                val intent = Intent.createChooser(IntentUtils.shareText(null, text), "Share deck")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun setupComponent() {
        getComponent(MultiExportComponent::class)
                .inject(this)
    }


    companion object {

        fun newInstance(): PtcgoExportFragment = PtcgoExportFragment()
    }
}