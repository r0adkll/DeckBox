package com.r0adkll.deckbuilder.arch.ui.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ftinc.kit.arch.di.HasComponent
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.snackbar
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.FlagPreferences
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseFragment
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionFragment
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionProgressController
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksFragment
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeModule
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportActivity
import com.r0adkll.deckbuilder.arch.ui.features.settings.SettingsActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.extensions.layoutHeight
import com.r0adkll.deckbuilder.util.extensions.partialPercentage
import com.r0adkll.deckbuilder.util.extensions.readablePercentage
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import javax.inject.Inject

class HomeActivity : BaseActivity(),
    HasComponent<HomeComponent>,
    CollectionProgressController {

    @Inject lateinit var editor: EditRepository
    @Inject lateinit var featureFlags: FlagPreferences

    private lateinit var component: HomeComponent
    private lateinit var adapter: HomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        adapter = HomePagerAdapter(supportFragmentManager)
        pager.adapter = adapter
        pager.offscreenPageLimit = 2

        bottomNavigation.setOnTabSelectListener({
            when (it) {
                R.id.tab_decks -> {
                    if (pager.currentItem != 0) {
                        pager.setCurrentItem(0, true)
                    }
                }
                R.id.tab_collection -> {
                    if (pager.currentItem != 1) {
                        pager.setCurrentItem(1, true)
                    }
                }
                R.id.tab_browser -> {
                    if (pager.currentItem != 2) {
                        pager.setCurrentItem(2, true)
                    }
                }
            }
        }, false)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == 0) {
                    interpolateProgressLayout(positionOffset)
                } else if (position == 1) {
                    interpolateProgressLayout(1 - positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {
                bottomNavigation.selectTabAtPosition(position, false)
            }
        })
    }

    override fun setupComponent() {
        this.component = DeckApp.component
            .plus(HomeModule(this))
        this.component.inject(this)
    }

    override fun getComponent(): HomeComponent = component

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val cards = DeckImportActivity.parseResults(resultCode, requestCode, data)
        cards?.let {
            if (it.isNotEmpty()) {
                Analytics.event(Event.SelectContent.Action("import_cards"))
                disposables += editor.submit(editor.createNewSession(), Edit.AddCards(it))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({ deckId ->
                        startActivity(DeckBuilderActivity.createIntent(this, deckId))
                    }, { t ->
                        Timber.e(t)
                        snackbar(R.string.error_session_new_deck)
                    })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_import -> {
                Analytics.event(Event.SelectContent.MenuAction("import_decklist"))
                DeckImportActivity.show(this)
                true
            }
            R.id.action_settings -> {
                Analytics.event(Event.SelectContent.MenuAction("settings"))
                startActivity(SettingsActivity.createIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setOverallProgress(progress: Float) {
        progressCompletion.text = getString(R.string.completion_format, progress.readablePercentage)
        progressView.progress = progress
    }

    private fun interpolateProgressLayout(progress: Float) {
        // 0: hidden, 1: shown
        progressLayout.alpha = progress.partialPercentage(PROGRESS_FADE_SEGMENT)

        val translationY = (progress * progressLayout.height)
        val height = dip(APPROX_APPBAR_HEIGHT) + translationY
        appBarLayout.layoutHeight(height.toInt())
        progressLayout.translationY = translationY
    }

    class HomePagerAdapter(
        fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> DecksFragment.newInstance()
            1 -> CollectionFragment.newInstance()
            2 -> BrowseFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid pager position")
        }

        override fun getCount(): Int = 3
    }

    companion object {
        private const val APPROX_APPBAR_HEIGHT = 56f
        private const val PROGRESS_FADE_SEGMENT = 0.2f

        fun createIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }
}
