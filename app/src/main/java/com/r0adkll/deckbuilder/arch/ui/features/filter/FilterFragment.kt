package com.r0adkll.deckbuilder.arch.ui.features.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.arch.ui.components.BaseFragment
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.FilterRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterModule
import com.r0adkll.deckbuilder.arch.ui.features.filter.di.FilterableComponent
import com.r0adkll.deckbuilder.arch.ui.features.search.DrawerInteractor
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import io.pokemontcg.model.Type
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class FilterFragment : BaseFragment(), FilterUi, FilterUi.Intentions, FilterUi.Actions {

    @com.evernote.android.state.State
    override var state: State = State.DEFAULT
    private val filterIntentions: FilterIntentions = FilterIntentions()

    @Inject lateinit var renderer: FilterRenderer
    @Inject lateinit var presenter: FilterPresenter
    @Inject lateinit var drawerInteractor: DrawerInteractor

    private lateinit var adapter: FilterRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { drawerInteractor.closeDrawer() }
        toolbar.inflateMenu(R.menu.fragment_filter)
        toolbar.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.action_clear_filter -> {
                    filterIntentions.clearFilter.accept(Unit)
                    true
                }
                else -> false
            }
        }

        adapter = FilterRecyclerAdapter(activity!!, filterIntentions)
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = adapter
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        if (activity is SearchActivity) {
            val type = (activity as SearchActivity).superType
            state = state.copy(category = type)
        }

        renderer.start()
        presenter.start()
    }

    override fun setupComponent() {
        getComponent(FilterableComponent::class)
                .plus(FilterModule(this))
                .inject(this)
    }

    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun fieldChanges(): Observable<SearchField> = filterIntentions.fieldChanges.doOnNext {
        Analytics.event(Event.SelectContent.FilterOption("search_field", it.name))
    }

    override fun typeClicks(): Observable<Pair<String, Type>> = filterIntentions.typeClicks.doOnNext {
        Analytics.event(Event.SelectContent.FilterOption(it.first, it.second.displayName))
    }

    override fun attributeClicks(): Observable<FilterAttribute> = filterIntentions.attributeClicks.doOnNext { attr ->
        Analytics.event(Event.SelectContent.FilterOption("attribute", when(attr) {
            is FilterAttribute.SuperTypeAttribute -> attr.superType.displayName
            is FilterAttribute.SubTypeAttribute -> attr.subType.displayName
            is FilterAttribute.ContainsAttribute -> attr.attribute
            is FilterAttribute.ExpansionAttribute -> attr.format.name.toLowerCase()
        }))
    }

    override fun optionClicks(): Observable<Pair<String, Any>> = filterIntentions.optionClicks.doOnNext { opt ->
        val option = opt.second
        Analytics.event(Event.SelectContent.FilterOption("option", when(option) {
            is Expansion -> option.code
            is Rarity -> option.key
            else -> option.toString()
        }))
    }

    override fun viewMoreClicks(): Observable<Unit> = filterIntentions.viewMoreClicks

    override fun valueRangeChanges(): Observable<Pair<String, Item.ValueRange.Value>> = filterIntentions.valueRangeChanges.doOnNext {
        Analytics.event(Event.SelectContent.FilterOption(it.first, it.second.modifier.name, it.second.value.toLong()))
    }

    override fun clearFilter(): Observable<Unit> = filterIntentions.clearFilter.doOnNext {
        Analytics.event(Event.SelectContent.MenuAction("clear_filter"))
    }

    override fun setIsEmpty(isEmpty: Boolean) {
        toolbar.menu.findItem(R.id.action_clear_filter)?.isVisible = !isEmpty
    }

    override fun setItems(items: List<Item>) {
        adapter.submitList(items)
    }
}
