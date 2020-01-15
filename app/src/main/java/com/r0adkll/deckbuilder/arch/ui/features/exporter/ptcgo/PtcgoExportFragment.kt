package com.r0adkll.deckbuilder.arch.ui.features.exporter.ptcgo

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.extensions.toast
import com.ftinc.kit.util.IntentUtils
import com.jakewharton.rxbinding2.view.clicks
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ExportTask
import com.r0adkll.deckbuilder.arch.domain.features.exporter.ptcgo.PtcgoExporter
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportComponent
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.AppSchedulers
import kotlinx.android.synthetic.main.fragment_ptcgo_export.*
import timber.log.Timber
import javax.inject.Inject

class PtcgoExportFragment : BaseFragment() {

    private val clipboard: ClipboardManager by lazy {
        requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Inject lateinit var schedulers: AppSchedulers
    @Inject lateinit var deckRepository: DeckRepository
    @Inject lateinit var editRepository: EditRepository
    @Inject lateinit var exporter: PtcgoExporter
    @Inject lateinit var task: ExportTask

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ptcgo_export, container, false)
    }

    @SuppressLint("RxSubscribeOnError")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        disposables += editRepository.observeSession(task.deckId)
            .flatMap { exporter.export(it.cards, it.name) }
            .subscribeOn(schedulers.comp)
            .observeOn(schedulers.main)
            .subscribe({
                deckList.text = it
            }, {
                Timber.e(it, "Error exporting deck")
                deckList.text = getText(R.string.error_exporting_deck)
            })

        disposables += actionCopy.clicks()
            .subscribe {
                Analytics.event(Event.SelectContent.Action("copy_decklist"))
                val text = deckList.text.toString()
                val clip = ClipData.newPlainText("Deckbox Deck", text)
                clipboard.setPrimaryClip(clip)
                toast(getString(R.string.deck_copied_format, "Deck"))
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_export_ptcgo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                Analytics.event(Event.Share("deck"))
                val text = deckList.text.toString()
                val intent = Intent.createChooser(IntentUtils.shareText("", text), "Share deck")
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
