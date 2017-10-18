package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.DeckBuilderPagerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_builder.*
import timber.log.Timber
import javax.inject.Inject


class DeckBuilderActivity : BaseActivity(), DeckBuilderUi, DeckBuilderUi.Intentions, DeckBuilderUi.Actions{

    @State
    override var state: DeckBuilderUi.State = DeckBuilderUi.State.DEFAULT

    @Inject lateinit var renderer: DeckBuilderRenderer
    @Inject lateinit var presenter: DeckBuilderPresenter

    private val pokemonCardClicks: Relay<PokemonCard> = PublishRelay.create()
    private val addPokemon: Relay<PokemonCard> = PublishRelay.create()

    private lateinit var adapter: DeckBuilderPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)
        StateSaver.restoreInstanceState(this, savedInstanceState)

//        val restoredState = savedInstanceState?.getParcelable<DeckBuilderUi.State>("ViewState")
//        restoredState?.let {
//            state = it
//            Timber.i("State Restore: $state")
//        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener {
            // TODO: Add some sort of persistance check here to make sure the user doesn't discard unsaved changes
            supportFinishAfterTransition()
        }

        adapter = DeckBuilderPagerAdapter(this, pokemonCardClicks)
        pager.adapter = adapter
        pager.offscreenPageLimit = 3
        tabs.setupWithViewPager(pager)
        fab.setOnClickListener {
            val intent = SearchActivity.createIntent(this)
            startActivityForResult(intent, SearchActivity.RC_PICK_CARD)
        }

        renderer.start()
        presenter.start()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putParcelable("ViewState", state)
        StateSaver.saveInstanceState(this, outState)
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = SearchActivity.parseResult(requestCode, resultCode, data)
        result?.let { addPokemon.accept(it) }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_deck_builder, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                // TODO: Save the built deck, or check if they can save
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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