package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.di.UnifiedSearchModule
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseFragment() {


    private lateinit var adapter: SearchResultsRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SearchResultsRecyclerAdapter(activity!!)
        adapter.setEmptyView(emptyView)
        adapter.setOnItemLongClickListener { true }
    }


    override fun setupComponent() {
        getComponent(DeckBuilderComponent::class)
                .plus(UnifiedSearchModule(this))
                .inject(this)
    }

}