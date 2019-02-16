package com.r0adkll.deckbuilder.arch.ui.features.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.adapter.CollectionRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.collection.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.collection.di.CollectionModule
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.di.HomeComponent
import com.r0adkll.deckbuilder.arch.ui.components.delegates.PresenterFragmentDelegate
import com.r0adkll.deckbuilder.arch.ui.components.delegates.RendererFragmentDelegate
import kotlinx.android.synthetic.main.fragment_collection.*
import javax.inject.Inject


class CollectionFragment : BaseFragment(), CollectionUi, CollectionUi.Intentions, CollectionUi.Actions {

    override var state: State = State.DEFAULT

    @Inject lateinit var presenter: CollectionPresenter
    @Inject lateinit var renderer: CollectionRenderer

    private lateinit var adapter: CollectionRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CollectionRecyclerAdapter(requireContext())
        adapter.setEmptyView(collectionEmptyView)
        adapter.setOnItemClickListener {
            if (it is Item.ExpansionSet) {
                startActivity(CollectionSetActivity.createIntent(requireContext(), it.expansion))
            }
        }

        collectionRecycler.adapter = adapter
        (collectionRecycler.layoutManager as? GridLayoutManager)?.apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = adapter.items[position]
                    return when (item) {
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

        delegates += PresenterFragmentDelegate(presenter)
        delegates += RendererFragmentDelegate(renderer)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
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