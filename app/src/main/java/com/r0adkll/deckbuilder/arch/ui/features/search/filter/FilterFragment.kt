package com.r0adkll.deckbuilder.arch.ui.features.search.filter


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.FilterRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_filter.*


class FilterFragment : BaseFragment(), FilterUi, FilterUi.Intentions, FilterUi.Actions {

    override var state: State = State.DEFAULT
    private val filterIntentions: FilterIntentions = FilterIntentions()


    private lateinit var adapter: FilterRecyclerAdapter


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_filter, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = FilterRecyclerAdapter(activity, filterIntentions)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter
    }


    override fun render(state: FilterUi.State) {
        this.state = state

    }


    override fun typeClicks(): Observable<Pair<String, Type>> = filterIntentions.typeClicks
    override fun attributeClicks(): Observable<SubType> = filterIntentions.attributeClicks
    override fun optionClicks(): Observable<Pair<String, Any>> = filterIntentions.optionClicks
    override fun viewMoreClicks(): Observable<Unit> = filterIntentions.viewMoreClicks
    override fun valueRangeChanges(): Observable<Pair<String, Item.ValueRange.Value>> = filterIntentions.valueRangeChanges


    override fun setItems(items: List<Item>) {
        adapter.setFilterItems(items)
    }
}