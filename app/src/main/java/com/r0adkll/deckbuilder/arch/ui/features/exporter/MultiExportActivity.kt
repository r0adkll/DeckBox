package com.r0adkll.deckbuilder.arch.ui.features.exporter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.exporter.di.MultiExportModule
import com.r0adkll.deckbuilder.arch.ui.features.exporter.ptcgo.PtcgoExportFragment
import com.r0adkll.deckbuilder.arch.ui.features.exporter.tournament.TournamentExportFragment
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindParcelable
import kotlinx.android.synthetic.main.activity_multi_export.*


class MultiExportActivity : BaseActivity() {

    private val deck: Deck by bindParcelable(EXTRA_DECK)

    private lateinit var adapter: ExportPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_export)

        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        adapter = ExportPagerAdapter(this, supportFragmentManager)
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(MultiExportModule(deck))
                .inject(this)
    }


    class ExportPagerAdapter(
            val context: Context,
            fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment = when(position) {
            0 -> TournamentExportFragment.newInstance()
            else -> PtcgoExportFragment.newInstance()
        }


        override fun getPageTitle(position: Int): CharSequence? = when(position) {
            0 -> context.getString(R.string.tab_export_tournament)
            else -> context.getString(R.string.tab_export_ptcgo)
        }

        override fun getCount(): Int = 2
    }


    companion object {
        const val EXTRA_DECK = "MultiExportActivity.Deck"

        fun createIntent(context: Context, deck: Deck): Intent {
            val intent = Intent(context, MultiExportActivity::class.java)
            intent.putExtra(EXTRA_DECK, deck)
            return intent
        }
    }
}