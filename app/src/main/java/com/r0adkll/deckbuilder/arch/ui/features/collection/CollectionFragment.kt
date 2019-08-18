package com.r0adkll.deckbuilder.arch.ui.features.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ftinc.kit.arch.presentation.delegates.StatefulFragmentDelegate
import com.ftinc.kit.kotlin.utils.ScreenUtils.smallestWidth
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
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
        )
        adapter.setEmptyView(collectionEmptyView)
        adapter.setOnItemClickListener {
            if (it is Item.ExpansionSet) {
                Analytics.event(Event.SelectContent.CollectionExpansionSet(it.expansion.code))
                startActivity(CollectionSetActivity.createIntent(requireContext(), it.expansion))
            }
        }

        collectionRecycler.adapter = adapter
        (collectionRecycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        (collectionRecycler.layoutManager as? GridLayoutManager)?.apply {
            spanCount = if (smallestWidth(requireContext().resources, com.ftinc.kit.kotlin.utils.ScreenUtils.Config.TABLET_10)) 6 else 3
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = adapter.items[position]
                    return when (item) {
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

        delegates += StatefulFragmentDelegate(renderer, Lifecycle.Event.ON_RESUME)
        delegates += StatefulFragmentDelegate(presenter, Lifecycle.Event.ON_RESUME)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun migrateClicks(): Observable<Unit> {
        return migrateClicks
    }

    override fun setItems(items: List<Item>) {
        adapter.setCollectionItems(items)
    }

    override fun hideError() {
        collectionEmptyView.setEmptyMessage(R.string.empty_collection)
    }

    override fun showError(description: String) {
        collectionEmptyView.emptyMessage = description
    }

    override fun showLoading(isLoading: Boolean) {
        collectionEmptyView.setLoading(isLoading)
    }

    companion object {

        fun newInstance(): CollectionFragment = CollectionFragment()
    }
}