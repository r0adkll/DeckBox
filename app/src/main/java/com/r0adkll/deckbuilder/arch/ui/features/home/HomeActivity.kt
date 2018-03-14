package com.r0adkll.deckbuilder.arch.ui.features.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseFragment
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
import gov.scstatehouse.houseofcards.di.HasComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : BaseActivity(), HasComponent<HomeComponent> {

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
                R.id.tab_browser -> {
                    if (pager.currentItem != 1) {
                        pager.setCurrentItem(1, true)
                    }
                }
            }
        }, false)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomNavigation.selectTabAtPosition(position, false)
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
                        .subscribe {
                            startActivity(DeckBuilderActivity.createIntent(this, it, true))
                        }
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
        this.component = component.plus(HomeModule())
        this.component.inject(this)
    }


    override fun getComponent(): HomeComponent = component


    class HomePagerAdapter(
            fragmentManager: FragmentManager
    ) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment? = when(position) {
            0 -> DecksFragment.newInstance()
            1 -> BrowseFragment.newInstance()
            else -> null
        }


        override fun getCount(): Int = 2 // TODO: Increase when we add more screens
    }
}
