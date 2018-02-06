package com.r0adkll.deckbuilder.arch.ui.features.browse


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.GridLayoutManager
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi.*
import com.r0adkll.deckbuilder.arch.ui.features.browse.di.SetBrowserModule
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonBuilderRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonItem
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindString
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_set_browser.*
import javax.inject.Inject


class SetBrowserActivity : BaseActivity(), SetBrowserUi, SetBrowserUi.Intentions, SetBrowserUi.Actions {

    private val setCode: String by bindString(EXTRA_SET_CODE)
    private val setName: String by bindString(EXTRA_SET_NAME)

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: SetBrowserRenderer
    @Inject lateinit var presenter: SetBrowserPresenter

    private val filterChanges: Relay<SetBrowserUi.BrowseFilter> = PublishRelay.create()
    private val cardClicks: Relay<PokemonCardView> = PublishRelay.create()
    private lateinit var adapter: PokemonBuilderRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_browser)

        state = state.copy(setCode = setCode)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        appbar?.setNavigationOnClickListener { finish() }

        // listen for tab changes
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val filter = when(tab.position) {
                    0 -> BrowseFilter.ALL
                    1 -> BrowseFilter.POKEMON
                    2 -> BrowseFilter.TRAINER
                    3 -> BrowseFilter.ENERGY
                    else -> BrowseFilter.ALL
                }
                filterChanges.accept(filter)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        disposables += cardClicks
                .subscribe {
                    Analytics.event(Event.SelectContent.PokemonCard(it.card?.id ?: "unknown"))
                    CardDetailActivity.show(this, it)
                }

        adapter = PokemonBuilderRecyclerAdapter(this, EditCardIntentions(), cardClicks)
        adapter.setEmptyView(emptyView)
        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = adapter

        renderer.start()
        presenter.start()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(SetBrowserModule(this))
                .inject(this)
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun render(state: SetBrowserUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun filterChanged(): Observable<SetBrowserUi.BrowseFilter> {
        return filterChanges
    }


    override fun setFilter(filter: SetBrowserUi.BrowseFilter) {
        val position = when(filter) {
            BrowseFilter.ALL -> 0
            BrowseFilter.POKEMON -> 1
            BrowseFilter.TRAINER -> 2
            BrowseFilter.ENERGY -> 3
        }
        tabs.getTabAt(position)?.select()
    }


    override fun setCards(cards: List<PokemonCard>) {
        adapter.setPokemon(cards.map { PokemonItem.Single(StackedPokemonCard(it, 1)) })
    }


    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }


    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }


    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_set_browse_message)
    }


    companion object {
        const val EXTRA_SET_NAME = "SetBrowserActivity.SetName"
        const val EXTRA_SET_CODE = "SetBrowserActivity.SetCode"

        fun createIntent(context: Context, setCode: String, setName: String): Intent {
            val intent = Intent(context, SetBrowserActivity::class.java)
            intent.putExtra(EXTRA_SET_CODE, setCode)
            intent.putExtra(EXTRA_SET_NAME, setName)
            return intent
        }
    }
}