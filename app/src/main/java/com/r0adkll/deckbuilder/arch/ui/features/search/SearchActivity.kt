package com.r0adkll.deckbuilder.arch.ui.features.search


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchModule
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject


class SearchActivity : BaseActivity(), SearchUi, SearchUi.Intentions, SearchUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: SearchRenderer
    @Inject lateinit var presenter: SearchPresenter

    private val categoryChanges: Relay<SuperType> = PublishRelay.create()
    private lateinit var adapter: SearchResultsRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        adapter = SearchResultsRecyclerAdapter(this)
        adapter.setEmptyView(empty_view)
        adapter.setOnItemClickListener { card ->
            // Select said pokemon card, or enter selection state, or what not
            val data = Intent()
            data.putExtra(EXTRA_SELECTED_CARD, card)
            setResult(RESULT_OK, data)
            supportFinishAfterTransition()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        searchback.setOnClickListener {
            supportFinishAfterTransition()
        }
    }


    override fun onStart() {
        super.onStart()
        renderer.start()
        presenter.start()
    }


    override fun onStop() {
        presenter.stop()
        renderer.stop()
        super.onStop()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(SearchModule(this))
                .inject(this)
    }


    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }


    override fun searchCards(): Observable<String> {
        return searchView.queryTextChanges()
                .map { it.toString() }
                .filter { it.length > 3 }
                .uiDebounce()
    }


    override fun switchCategories(): Observable<SuperType> {
        return categoryChanges
    }


    override fun setResults(cards: List<PokemonCard>) {
        adapter.setCards(cards)
    }


    override fun showLoading(isLoading: Boolean) {
        empty_view.setLoading(isLoading)
    }


    override fun showError(description: String) {
        empty_view.emptyMessage = description
    }


    override fun hideError() {
        empty_view.setEmptyMessage(R.string.empty_search_category)
    }


    override fun setCategory(superType: SuperType) {
        val position = when(superType) {
            SuperType.POKEMON -> 0
            SuperType.TRAINER -> 1
            SuperType.ENERGY -> 2
            else -> 0
        }
        tabs.getTabAt(position)?.select()
    }


    companion object {
        @JvmField val RC_PICK_CARD = 100
        @JvmField val EXTRA_SELECTED_CARD = "com.r0adkll.deckbuilder.intent.EXTRA_SELECTED_CARD"


        fun createIntent(context: Context): Intent = Intent(context, SearchActivity::class.java)


        fun parseResult(requestCode: Int, resultCode: Int, data: Intent?): PokemonCard? {
            return if (requestCode == RC_PICK_CARD && resultCode == RESULT_OK) {
                data?.getParcelableExtra(EXTRA_SELECTED_CARD)
            } else {
                null
            }
        }
    }
}