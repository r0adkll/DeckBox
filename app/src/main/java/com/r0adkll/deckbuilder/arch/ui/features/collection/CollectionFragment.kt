package com.r0adkll.deckbuilder.arch.ui.features.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ftinc.kit.arch.presentation.BaseFragment
import com.ftinc.kit.arch.presentation.delegates.StatefulFragmentDelegate
import com.ftinc.kit.util.ScreenUtils
import com.ftinc.kit.util.ScreenUtils.smallestWidth
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.adapter.CollectionRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.collection.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.collection.di.CollectionModule
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_collection.*
import javax.inject.Inject

class CollectionFragment : BaseFragment(), CollectionUi, CollectionUi.Intentions, CollectionUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var presenter: CollectionPresenter
    @Inject lateinit var renderer: CollectionRenderer
    @Inject lateinit var preferences: AppPreferences

    private val migrateClicks = PublishRelay.create<Unit>()
    private lateinit var adapter: CollectionRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CollectionRecyclerAdapter(requireContext(), migrateClicks,
            dismissClicks = {
                preferences.showCollectionMigration.set(false)
            }
        ) {
            if (it is Item.ExpansionSet) {
                Analytics.event(Event.SelectContent.CollectionExpansionSet(it.expansion.code))
                startActivity(CollectionSetActivity.createIntent(requireContext(), it.expansion))
            }
        }
        adapter.emptyView = collectionEmptyView

        collectionRecycler.adapter = adapter
        (collectionRecycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        (collectionRecycler.layoutManager as? GridLayoutManager)?.apply {
            spanCount = if (smallestWidth(requireContext().resources, ScreenUtils.Config.TABLET_10)) {
                TABLET_SPAN_COUNT
            } else {
                PHONE_SPAN_COUNT
            }
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.currentList[position]) {
                        is Item.Migration -> spanCount
                        is Item.ExpansionSeries -> spanCount
                        else -> 1
                    }
                }
            }
        }
    }

    override fun setupComponent() {
        getComponent(HomeComponent::class)
            .plus(CollectionModule(this))
            .inject(this)

        delegates += StatefulFragmentDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulFragmentDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun migrateClicks(): Observable<Unit> {
        return migrateClicks
    }

    override fun setItems(items: List<Item>) {
        adapter.submitList(items)
    }

    override fun hideError() {
        collectionEmptyView.setMessage(R.string.empty_collection)
    }

    override fun showError(description: String) {
        collectionEmptyView.message = description
    }

    override fun showLoading(isLoading: Boolean) {
        collectionEmptyView.state = if (isLoading) {
            EmptyView.State.LOADING
        } else {
            EmptyView.State.EMPTY
        }
    }

    companion object {
        private const val TABLET_SPAN_COUNT = 6
        private const val PHONE_SPAN_COUNT = 3

        fun newInstance(): CollectionFragment = CollectionFragment()
    }
}
