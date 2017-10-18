package com.r0adkll.deckbuilder.arch.ui.features.search


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.search.di.SearchModule
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.pageadapter.KeyboardScrollHideListener
import com.r0adkll.deckbuilder.arch.ui.features.search.pageadapter.ResultsPagerAdapter
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.OnTabSelectedAdapter
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import com.r0adkll.deckbuilder.util.findEnum
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject


class SearchActivity : BaseActivity(), SearchUi, SearchUi.Intentions, SearchUi.Actions {

    @com.evernote.android.state.State
    override var state: State = State.DEFAULT

    @com.evernote.android.state.State
    var superType: SuperType = SuperType.POKEMON

    @Inject lateinit var renderer: SearchRenderer
    @Inject lateinit var presenter: SearchPresenter

    private val categoryChanges: Relay<SuperType> = PublishRelay.create()
    private val pokemonCardClicks: Relay<PokemonCard> = PublishRelay.create()
    private lateinit var adapter: ResultsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        adapter = ResultsPagerAdapter(this, KeyboardScrollHideListener(searchView), pokemonCardClicks)
        pager.offscreenPageLimit = 3
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)

        searchback.setOnClickListener {
            supportFinishAfterTransition()
        }

        tabs.addOnTabSelectedListener(object : OnTabSelectedAdapter() {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val category = when(tab.position) {
                    0 -> SuperType.POKEMON
                    1 -> SuperType.TRAINER
                    2 -> SuperType.ENERGY
                    else -> SuperType.POKEMON
                }
                categoryChanges.accept(category)
            }
        })


        superType = findEnum<SuperType>(EXTRA_SUPER_TYPE) ?: SuperType.POKEMON

        // Set default tab
        val i = when(superType) {
            SuperType.POKEMON -> 0
            SuperType.TRAINER -> 1
            SuperType.ENERGY -> 2
            else -> 0
        }
        tabs.getTabAt(i)?.select()

        state = state.copy(category = superType)
    }


    override fun onStart() {
        super.onStart()
        renderer.start()
        presenter.start()
    }


    override fun onStop() {
        presenter.stop()
        renderer.stop()
        super.onStop()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(SearchModule(this))
                .inject(this)
    }


    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }


    override fun searchCards(): Observable<String> {
        return searchView.queryTextChanges()
                .map { it.toString() }
                .filter { it.length > 3 }
                .uiDebounce()
    }


    override fun switchCategories(): Observable<SuperType> {
        return categoryChanges
    }


    override fun selectCard(): Observable<PokemonCard> {
        return pokemonCardClicks
    }


    override fun setQueryText(text: String) {
        searchView.setQuery(text, false)
    }


    override fun setResults(superType: SuperType, cards: List<PokemonCard>) {
        adapter.setCards(superType, cards)
    }


    override fun setSelectedCards(cards: List<PokemonCard>) {
        val data = Intent()
        data.putParcelableArrayListExtra(EXTRA_SELECTED_CARDS, ArrayList(cards))
        setResult(RESULT_OK, data)
    }


    override fun showLoading(superType: SuperType, isLoading: Boolean) {
        adapter.showLoading(superType, isLoading)
    }


    override fun showError(superType: SuperType, description: String) {
        adapter.showError(superType, description)
    }


    override fun hideError(superType: SuperType) {
        adapter.hideError(superType)
    }


    override fun setCategory(superType: SuperType) {
        this.superType = superType
        val position = when(superType) {
            SuperType.POKEMON -> 0
            SuperType.TRAINER -> 1
            SuperType.ENERGY -> 2
            else -> 0
        }
        tabs.getTabAt(position)?.select()
    }


    companion object {
        @JvmField val RC_PICK_CARD = 100
        @JvmField val EXTRA_SELECTED_CARDS = "com.r0adkll.deckbuilder.intent.EXTRA_SELECTED_CARDS"
        @JvmField val EXTRA_SUPER_TYPE = "SuperType"


        fun createIntent(context: Context, superType: SuperType = SuperType.POKEMON): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(EXTRA_SUPER_TYPE, superType)
            return intent
        }


        fun parseResult(requestCode: Int, resultCode: Int, data: Intent?): List<PokemonCard>? {
            return if (requestCode == RC_PICK_CARD && resultCode == RESULT_OK) {
                data?.getParcelableArrayListExtra(EXTRA_SELECTED_CARDS)
            } else {
                null
            }
        }
    }
}