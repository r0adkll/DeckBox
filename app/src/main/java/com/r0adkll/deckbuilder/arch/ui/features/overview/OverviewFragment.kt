package com.r0adkll.deckbuilder.arch.ui.features.overview

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.PresenterFragmentDelegate
import com.ftinc.kit.arch.presentation.delegates.RendererFragmentDelegate
import com.ftinc.kit.kotlin.utils.bindLong
import com.ftinc.kit.kotlin.utils.bundle
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.overview.adapter.OverviewRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.overview.di.OverviewModule
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject


class OverviewFragment : BaseFragment(), OverviewUi, OverviewUi.Intentions, OverviewUi.Actions {

    override var state: OverviewUi.State = OverviewUi.State.DEFAULT

    private val sessionId by bindLong(EXTRA_SESSION_ID, Session.NO_ID)
    private val editCardIntentions: EditCardIntentions = EditCardIntentions()
    private val cardClicks: Relay<PokemonCardView> = PublishRelay.create()

    @Inject lateinit var presenter: OverviewPresenter
    @Inject lateinit var renderer: OverviewRenderer

    private lateinit var adapter: OverviewRecyclerAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        state = state.copy(sessionId = sessionId)

        // Now call this since it will trigger presenter.start()
        super.onActivityCreated(savedInstanceState)

        adapter = OverviewRecyclerAdapter(activity!!, cardClicks, editCardIntentions)
        adapter.setEmptyView(emptyView)
        val layoutManager = GridLayoutManager(activity!!, 7)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapter.items[position]
                return item.size
            }
        }
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter

        disposables += cardClicks.subscribe {
            CardDetailActivity.show(activity!!, it, sessionId)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }


    override fun setupComponent() {
        DeckApp.component.plus(OverviewModule(this))
                .inject(this)

        addDelegate(RendererFragmentDelegate(renderer))
        addDelegate(PresenterFragmentDelegate(presenter))
    }


    override fun render(state: OverviewUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun addCards(): Observable<List<PokemonCard>> {
        return editCardIntentions.addCardClicks
    }


    override fun removeCard(): Observable<PokemonCard> {
        return editCardIntentions.removeCardClicks
    }


    override fun showCards(cards: List<EvolutionChain>) {
        adapter.setCards(cards)
    }


    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }


    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }


    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_deck_overview)
    }


    companion object {
        const val TAG = "OverviewFragment"
        private const val EXTRA_SESSION_ID = "OverviewFragment.SessionId"


        fun newInstance(sessionId: Long): OverviewFragment {
            val fragment = OverviewFragment()
            fragment.arguments = bundle { EXTRA_SESSION_ID to sessionId }
            return fragment
        }
    }
}