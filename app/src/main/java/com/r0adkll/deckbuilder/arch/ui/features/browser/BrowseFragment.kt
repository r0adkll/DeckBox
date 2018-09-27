package com.r0adkll.deckbuilder.arch.ui.features.browser

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserActivity
import com.r0adkll.deckbuilder.arch.ui.features.browser.adapter.ExpansionRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.browser.di.BrowseModule
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.ScreenUtils
import com.r0adkll.deckbuilder.util.ScreenUtils.smallestWidth
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_browse.*
import javax.inject.Inject


class BrowseFragment : BaseFragment(), BrowseUi, BrowseUi.Actions, BrowseUi.Intentions {

    override var state: BrowseUi.State = BrowseUi.State.DEFAULT

    @Inject lateinit var renderer: BrowseRenderer
    @Inject lateinit var presenter: BrowsePresenter

    private lateinit var adapter: ExpansionRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ExpansionRecyclerAdapter(activity!!)
        adapter.setEmptyView(emptyView)
        adapter.setOnItemClickListener {
            Analytics.event(Event.SelectContent.Action("expansion", it.name))
            val intent = SetBrowserActivity.createIntent(activity!!, it)
            startActivity(intent)
        }
        recycler.layoutManager = if (smallestWidth(ScreenUtils.Config.TABLET_10)) {
            GridLayoutManager(activity!!, 3)
        } else {
            LinearLayoutManager(activity!!)
        }
        recycler.adapter = adapter

        swipeRefresh.setColorSchemeResources(
                R.color.poketype_fire,
                R.color.poketype_grass,
                R.color.poketype_water,
                R.color.poketype_electric,
                R.color.poketype_fighting,
                R.color.poketype_psychic,
                R.color.poketype_steel,
                R.color.poketype_dragon,
                R.color.poketype_fairy,
                R.color.poketype_dark
        )

        renderer.start()
        presenter.start()

        actionSearch.setOnClickListener {
            startActivity(SearchActivity.createIntent(it.context))
        }
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun setupComponent() {
        getComponent(HomeComponent::class)
                .plus(BrowseModule(this))
                .inject(this)
    }


    override fun render(state: BrowseUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun refreshExpansions(): Observable<Unit> {
        return swipeRefresh.refreshes()
    }


    override fun setExpansions(expansions: List<Expansion>) {
        adapter.setExpansions(expansions)
    }


    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
        swipeRefresh.isRefreshing = isLoading
    }


    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }


    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_browse_message)
    }


    companion object {

        fun newInstance(): BrowseFragment = BrowseFragment()
    }
}