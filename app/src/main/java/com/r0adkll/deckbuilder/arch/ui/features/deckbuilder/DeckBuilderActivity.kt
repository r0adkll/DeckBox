package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.DeckBuilderPagerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_builder.*
import javax.inject.Inject


class DeckBuilderActivity : BaseActivity(), DeckBuilderUi, DeckBuilderUi.Intentions, DeckBuilderUi.Actions{

    @Inject lateinit var renderer: DeckBuilderRenderer
    @Inject lateinit var presenter: DeckBuilderPresenter

    private val pokemonCardClicks: Relay<PokemonCard> = PublishRelay.create()
    private val addPokemon: Relay<PokemonCard> = PublishRelay.create()

    private lateinit var adapter: DeckBuilderPagerAdapter

    override var state: State = State.DEFAULT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)

        adapter = DeckBuilderPagerAdapter(layoutInflater, pokemonCardClicks)
        pager.adapter = adapter
        pager.offscreenPageLimit = 3
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                tabs.getTabAt(position)?.select()
            }
        })

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.setCurrentItem(tab.position, true)
            }
        })

        fab.setOnClickListener {

        }
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
        component.plus(DeckBuilderModule(this))
                .inject(this)
    }


    override fun render(state: DeckBuilderUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun addCard(): Observable<PokemonCard> {
        return addPokemon
    }


    override fun removeCard(): Observable<PokemonCard> {
        return Observable.empty()
    }


    override fun editDeckName(): Observable<String> {
        return Observable.empty()
    }


    override fun editDeckDescription(): Observable<String> {
        return Observable.empty()
    }


    override fun showPokemonCards(cards: List<PokemonCard>) {
        adapter.setCards(SuperType.POKEMON, cards)
    }


    override fun showTrainerCards(cards: List<PokemonCard>) {
        adapter.setCards(SuperType.TRAINER, cards)
    }


    override fun showEnergyCards(cards: List<PokemonCard>) {
        adapter.setCards(SuperType.ENERGY, cards)
    }


    override fun showDeckName(name: String) {

    }


    override fun showDeckDescription(description: String) {

    }


    companion object {
        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
    }
}