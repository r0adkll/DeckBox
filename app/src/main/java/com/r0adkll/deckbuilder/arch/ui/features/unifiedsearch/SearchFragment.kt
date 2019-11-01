package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch

import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.arch.di.HasComponent
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.StatefulFragmentDelegate
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterIntentions
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableComponent
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableModule
import com.r0adkll.deckbuilder.arch.ui.features.search.DrawerInteractor
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.search.pageadapter.KeyboardScrollHideListener
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchComponent
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchModule
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.ImeUtils
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : BaseFragment(),
    SearchUi,
    SearchUi.Intentions,
    SearchUi.Actions,
    FilterIntentions,
    DrawerInteractor,
    HasComponent<FilterableComponent> {

    private val drawer: DrawerLayout by lazy { view as DrawerLayout }

    override var state: SearchUi.State = SearchUi.State.DEFAULT

    @Inject lateinit var renderer: SearchRenderer
    @Inject lateinit var presenter: SearchPresenter

    private val filterChanges: Relay<Pair<SuperType, Filter>> = PublishRelay.create()
    private lateinit var adapter: SearchResultsRecyclerAdapter
    private lateinit var component: UnifiedSearchComponent
    private lateinit var toolbarScrollListener: ToolbarScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SearchResultsRecyclerAdapter(activity!!, true)
        adapter.emptyView = emptyView
        adapter.onItemClickListener = { v, _ ->
            // FIXME: Do something about this god-awful mess
            val card = v.findViewById<PokemonCardView>(R.id.card)
            Analytics.event(Event.SelectContent.PokemonCard(card.card?.id ?: "unknown"))
            CardDetailActivity.show(activity!!, card)
        }

        actionFilter.setOnClickListener {
            Analytics.event(Event.SelectContent.MenuAction("show_filter"))
            drawer.openDrawer(GravityCompat.END)
            ImeUtils.hideIme(searchView)
        }

        recycler.layoutManager = GridLayoutManager(activity!!, 6)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        recycler.setItemViewCacheSize(20)

        recycler.addOnScrollListener(KeyboardScrollHideListener(searchView))

        toolbarScrollListener = ToolbarScrollListener(searchToolbar)
        recycler.addOnScrollListener(toolbarScrollListener)

        recycler.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> ImeUtils.hideIme(searchView)
            }
            false
        }
    }

    override fun setupComponent() {
        component = getComponent(DeckBuilderComponent::class)
            .unifiedSearchComponentBuilder()
            .unifiedSearchModule(UnifiedSearchModule(this))
            .filterableModule(FilterableModule(this, this))
            .build()
        component.inject(this)

        delegates += StatefulFragmentDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulFragmentDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun getComponent(): FilterableComponent {
        return component
    }

    override fun render(state: SearchUi.State) {
        this.state = state
        renderer.render(state)
    }

    override fun filterUpdates(): Observable<Pair<SuperType, Filter>> {
        return filterChanges
    }

    override fun filterChanges(): Relay<Pair<SuperType, Filter>> {
        return filterChanges
    }

    override fun categoryChange(): Observable<SuperType> {
        return Observable.just(SuperType.UNKNOWN)
    }

    override fun searchCards(): Observable<String> {
        return searchView.queryTextChanges()
            .map { it.toString() }
            .uiDebounce(500L)
            .doOnNext {
                Analytics.event(Event.Search(it))
                if (it.isBlank()) toolbarScrollListener.reset()
            }
    }

    override fun showFilterEmpty(enabled: Boolean) {
        actionFilter.setImageResource(when (enabled) {
            true -> R.drawable.ic_filter_outline
            false -> R.drawable.ic_filter_filled
        })
    }

    override fun setQueryText(text: String) {
        searchView.setQuery(text, false)
    }

    override fun setResults(cards: List<PokemonCard>) {
        adapter.submitList(cards)
    }

    override fun showEmptyResults() {
        emptyView.setMessage(R.string.empty_search_results_category)
    }

    override fun showEmptyDefault() {
        emptyView.setMessage(R.string.empty_search_category)
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.state = if (isLoading) {
            EmptyView.State.LOADING
        } else {
            EmptyView.State.EMPTY
        }
    }

    override fun showError(description: String) {
        emptyView.message = description
    }

    override fun hideError() {
        emptyView.setMessage(R.string.empty_search_category)
    }

    override fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.END)
    }

    fun wiggleCard(card: PokemonCard) {
        val adapterPosition = adapter.indexOf(card)
        if (adapterPosition != RecyclerView.NO_POSITION) {
            val layoutManager = recycler.layoutManager as GridLayoutManager
            val childIndex = adapterPosition - layoutManager.findFirstVisibleItemPosition()
            val child = layoutManager.getChildAt(childIndex)
            child?.let {
                val rotateAnim = RotateAnimation(-5f, 5f, Animation.RELATIVE_TO_SELF, .5f,
                    Animation.RELATIVE_TO_SELF, .5f)
                rotateAnim.repeatCount = 3
                rotateAnim.repeatMode = Animation.REVERSE
                rotateAnim.duration = 50

                val transAnim = TranslateAnimation(0f, 0f, 0f, -it.dp(8))
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

    class ToolbarScrollListener(
        private val toolBar: View
    ) : RecyclerView.OnScrollListener() {

        private val elevation: Float by lazy { toolBar.dp(4) }
        private var scrollY: Float = 0f

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            scrollY += dy
            toolBar.elevation = scrollY.coerceIn(0f, elevation)
        }

        fun reset() {
            scrollY = 0f
            toolBar.elevation = 0f
        }
    }

    companion object {
        const val TAG = "UnifiedSearchFragment"
    }
}
