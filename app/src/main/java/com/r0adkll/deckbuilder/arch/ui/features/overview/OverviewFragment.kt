package com.r0adkll.deckbuilder.arch.ui.features.overview

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.StatefulFragmentDelegate
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.util.bindLong
import com.ftinc.kit.util.bundle
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.SessionId
import com.r0adkll.deckbuilder.arch.ui.features.overview.adapter.OverviewRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.overview.di.OverviewModule
import com.r0adkll.deckbuilder.arch.ui.features.overview.di.OverviewableComponent
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.ScreenUtils.orientation
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_overview.*
import javax.inject.Inject

class OverviewFragment : BaseFragment(), OverviewUi, OverviewUi.Intentions, OverviewUi.Actions {

    override var state: OverviewUi.State = OverviewUi.State.DEFAULT

    private val sessionIdByIntent by bindLong(EXTRA_SESSION_ID, Session.NO_ID)
    private val editCardIntentions: EditCardIntentions = EditCardIntentions()
    private val cardClicks: Relay<PokemonCardView> = PublishRelay.create()

    @JvmField @field:[Inject SessionId]
    var sessionIdByInject: Long = Session.NO_ID
    @Inject lateinit var presenter: OverviewPresenter
    @Inject lateinit var renderer: OverviewRenderer

    private lateinit var adapter: OverviewRecyclerAdapter

    private val sessionId
        get() = if (sessionIdByInject != Session.NO_ID) {
            sessionIdByInject
        } else {
            sessionIdByIntent
        }

    @SuppressLint("RxSubscribeOnError")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        state = state.copy(sessionId = sessionId)

        adapter = OverviewRecyclerAdapter(requireContext(), cardClicks, editCardIntentions)
        adapter.emptyView = emptyView
        val spanCount = if (orientation(ORIENTATION_LANDSCAPE)) LANDSCAPE_SPAN_SIZE else PORTRAIT_SPAN_SIZE
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val item = adapter.currentList[position]
                return item.size
            }
        }
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter

        disposables += cardClicks.subscribe {
            CardDetailActivity.show(requireActivity(), it, sessionId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun setupComponent() {
        getComponent(OverviewableComponent::class)
            .plus(OverviewModule(this))
            .inject(this)

        delegates += StatefulFragmentDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulFragmentDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun render(state: OverviewUi.State) {
        this.state = state
        renderer.render(state)
    }

    override fun addCards(): Observable<List<PokemonCard>> {
        return editCardIntentions.addCardClicks
            .doOnNext { Analytics.event(Event.SelectContent.Action("edit_add_card")) }
    }

    override fun removeCard(): Observable<PokemonCard> {
        return editCardIntentions.removeCardClicks
            .doOnNext { Analytics.event(Event.SelectContent.Action("edit_remove_card")) }
    }

    override fun showCards(cards: List<EvolutionChain>) {
        adapter.submitList(cards)
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
        emptyView.setMessage(R.string.empty_deck_overview)
    }

    companion object {
        const val TAG = "OverviewFragment"
        private const val EXTRA_SESSION_ID = "OverviewFragment.SessionId"
        private const val LANDSCAPE_SPAN_SIZE = 7
        private const val PORTRAIT_SPAN_SIZE = 4

        fun newInstance(sessionId: Long): OverviewFragment {
            val fragment = OverviewFragment()
            fragment.arguments = bundle { EXTRA_SESSION_ID to sessionId }
            return fragment
        }
    }
}
