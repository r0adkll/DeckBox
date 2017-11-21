package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchModule
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseFragment(), SearchUi, SearchUi.Intentions, SearchUi.Actions{

    override var state: SearchUi.State = SearchUi.State.DEFAULT

    private lateinit var adapter: SearchResultsRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SearchResultsRecyclerAdapter(activity!!)
        adapter.setEmptyView(emptyView)
        adapter.setOnItemLongClickListener { v, card ->

            true
        }

        recycler.layoutManager = GridLayoutManager(activity!!, 3)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)

    }


    override fun render(state: SearchUi.State) {
        this.state = state

    }


    override fun filterUpdates(): Observable<Pair<SuperType, Filter>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun searchCards(): Observable<String> {
        return searchView.queryTextChanges()
                .map { it.toString() }
                .uiDebounce(500L)
    }


    override fun showFilterEmpty(enabled: Boolean) {
        actionFilter.setImageResource(when(enabled) {
            true -> R.drawable.ic_filter_outline
            false -> R.drawable.ic_filter_filled
        })
    }


    override fun setQueryText(text: String) {
        searchView.setQuery(text, false)
    }


    override fun setResults(cards: List<PokemonCard>) {
        adapter.setCards(cards)
    }


    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }


    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }


    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_search_category)
    }


    fun wiggleCard(card: PokemonCard) {
        val adapterPosition = adapter.indexOf(card)
        if (adapterPosition != RecyclerView.NO_POSITION) {
            val childIndex = adapterPosition - (recycler.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            val child = recycler.layoutManager.getChildAt(childIndex)
            child?.let {
                val rotateAnim = RotateAnimation(-5f, 5f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
                rotateAnim.repeatCount = 3
                rotateAnim.repeatMode = Animation.REVERSE
                rotateAnim.duration = 50

                val transAnim = TranslateAnimation(0f, 0f, 0f, -it.dpToPx(8f))
                transAnim.repeatCount = 1
                transAnim.repeatMode = Animation.REVERSE
                transAnim.duration = 100

                val set = AnimationSet(true)
                set.addAnimation(rotateAnim)
                set.addAnimation(transAnim)
                set.interpolator = AccelerateDecelerateInterpolator()

                it.startAnimation(set)
            }
        }
    }

    override fun setupComponent() {
        getComponent(DeckBuilderComponent::class)
                .plus(UnifiedSearchModule(this))
                .inject(this)
    }

}