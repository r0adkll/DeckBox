package com.r0adkll.deckbuilder.arch.ui.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.ftinc.kit.kotlin.extensions.setVisible
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
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
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.internal.di.HasComponent
import com.r0adkll.deckbuilder.util.extensions.snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt


class HomeActivity : BaseActivity(), HasComponent<HomeComponent>, CollectionProgressController {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }


    @Inject lateinit var editor: EditRepository

    private lateinit var component: HomeComponent
    private lateinit var adapter: HomePagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        adapter = HomePagerAdapter(supportFragmentManager)
        pager.adapter = adapter

        bottomNavigation.setOnTabSelectListener({
            when(it) {
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

        pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomNavigation.selectTabAtPosition(position, false)
                progressLayout.setVisible(position == 1)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val cards = DeckImportActivity.parseResults(resultCode, requestCode, data)
        cards?.let {
            if (it.isNotEmpty()) {
                Analytics.event(Event.SelectContent.Action("import_cards"))
                disposables += editor.createSession(imports = it)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe({ sessionId ->
                            startActivity(DeckBuilderActivity.createIntent(this, sessionId, true))
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
        return when(item.itemId) {
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


    override fun setupComponent(component: AppComponent) {
        this.component = component.plus(HomeModule(this))
        this.component.inject(this)
    }


    override fun getComponent(): HomeComponent = component


    override fun setOverallProgress(progress: Float) {
        progressCompletion.text = getString(R.string.completion_format, (progress.times(100f).roundToInt().coerceIn(0, 100)))
        progressView.progress = progress
    }


    class HomePagerAdapter(
            fragmentManager: androidx.fragment.app.FragmentManager
    ) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): androidx.fragment.app.Fragment? = when(position) {
            0 -> DecksFragment.newInstance()
            1 -> CollectionFragment.newInstance()
            2 -> BrowseFragment.newInstance()
            else -> null
        }


        override fun getCount(): Int = 3 // TODO: Increase when we add more screens
    }
}
