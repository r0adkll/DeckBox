@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.features.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.evernote.android.state.State
import com.ftinc.kit.arch.di.HasComponent
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.util.bindOptionalString
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterIntentions
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableComponent
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableModule
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.KeyboardScrollHideListener
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchModule
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.ImeUtils
import com.r0adkll.deckbuilder.util.extensions.setLoading
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import javax.inject.Inject

class SearchActivity : BaseActivity(),
    SearchUi,
    SearchUi.Intentions,
    SearchUi.Actions,
    FilterIntentions,
    DrawerInteractor,
    HasComponent<FilterableComponent> {

    @State override var state: SearchUi.State = SearchUi.State.DEFAULT

    val deckId by bindOptionalString(EXTRA_DECK_ID)

    @Inject lateinit var renderer: SearchRenderer
    @Inject lateinit var presenter: SearchPresenter

    private val editCardIntentions: EditCardIntentions = EditCardIntentions()
    private val pokemonCardLongClicks: Relay<PokemonCardView> = PublishRelay.create()
    private val filterChanges: Relay<Filter> = PublishRelay.create()
    private lateinit var adapter: SearchResultsRecyclerAdapter
    private lateinit var component: SearchComponent

    @SuppressLint("RxSubscribeOnError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        adapter = SearchResultsRecyclerAdapter(this, editCardIntentions = editCardIntentions)
        adapter.emptyView = emptyView

        if (deckId != null) {
            adapter.onItemClickListener = { _, card ->
                editCardIntentions.addCardClicks.accept(listOf(card))
            }
            adapter.onItemLongClickListener = { view, _ ->
                val card = view.findViewById<PokemonCardView>(R.id.card)
                pokemonCardLongClicks.accept(card)
                true
            }
        } else {
            adapter.onItemClickListener = { view, _ ->
                val card = view.findViewById<PokemonCardView>(R.id.card)
                pokemonCardLongClicks.accept(card)
            }
        }

        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        recycler.addOnScrollListener(KeyboardScrollHideListener(searchView))

        searchback.setOnClickListener {
            supportFinishAfterTransition()
        }

        actionFilter.setOnClickListener {
            Analytics.event(Event.SelectContent.MenuAction("show_filter"))
            drawer.openDrawer(GravityCompat.END)
            ImeUtils.hideIme(searchView)
        }

        disposables += pokemonCardLongClicks
            .subscribe {
                Analytics.event(Event.SelectContent.PokemonCard(it.card?.id ?: "unknown"))
                CardDetailActivity.show(this, it, deckId)
            }

        state = state.copy(
            id = UUID.randomUUID().toString(),
            deckId = deckId
        )

        searchView.post {
            searchView.requestFocus()
            ImeUtils.showIme(searchView)
        }
    }

    override fun setupComponent() {
        this.component = DeckApp.component.searchComponentBuilder()
            .searchModule(SearchModule(this))
            .filterableModule(FilterableModule(this, this))
            .build()
        this.component.inject(this)

        delegates += StatefulActivityDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulActivityDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun getComponent(): FilterableComponent {
        return component
    }

    override fun onPause() {
        super.onPause()
        ImeUtils.hideIme(searchView)
    }

    override fun render(state: SearchUi.State) {
        this.state = state
        renderer.render(state)
    }

    override fun searchCards(): Observable<String> {
        return searchView.queryTextChanges()
            .map { it.toString() }
            .uiDebounce(500L)
            .doOnNext {
                Analytics.event(Event.Search(it))
            }
    }

    override fun selectCard(): Observable<PokemonCard> {
        return editCardIntentions.addCardClicks
            .map { it.first() }
            .doOnNext { Analytics.event(Event.SelectContent.PokemonCard(it.id)) }
    }

    override fun removeCard(): Observable<PokemonCard> {
        return editCardIntentions.removeCardClicks
    }

    /*
     * This receives changes from the FilterPresenter
     */
    override fun filterChanges(): Relay<Filter> {
        return filterChanges
    }

    /*
     * This sends changes to the SearchPresenter
     */
    override fun filterUpdates(): Observable<Filter> {
        return filterChanges
    }

    override fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.END)
    }

    override fun showFilterEmpty(enabled: Boolean) {
        actionFilter.setImageResource(when (enabled) {
            true -> R.drawable.ic_filter_outline
            false -> R.drawable.ic_filter_filled
        })
    }

    override fun setResults(cards: List<PokemonCard>) {
        adapter.submitList(cards)
    }

    override fun setSelectedCards(cards: List<PokemonCard>) {
        adapter.setSelectedCards(cards)
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }

    override fun showEmptyResults() {
        emptyView.setMessage(R.string.empty_search_results_pokemon_message)
    }

    override fun showEmptyDefault() {
        emptyView.setMessage(R.string.empty_search_pokemon_message)
    }

    override fun showError(description: String) {
        emptyView.message = description
    }

    override fun hideError() {
        showEmptyDefault()
    }

    companion object {
        private const val EXTRA_DECK_ID = "SearchActivity.DeckId"

        fun createIntent(
            context: Context,
            deckId: String? = null
        ): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            if (deckId != null) {
                intent.putExtra(EXTRA_DECK_ID, deckId)
            }
            return intent
        }

        fun parseResult(data: Intent?): String? {
            return data?.getStringExtra(EXTRA_DECK_ID)
        }
    }
}
