package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.DragEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.evernote.android.state.State
import com.ftinc.kit.kotlin.extensions.*
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter.DeckBuilderPagerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.sothree.slidinguppanel.SlidingUpPanelLayout
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
    private val addPokemon: Relay<List<PokemonCard>> = PublishRelay.create()
    private val removePokemon: Relay<PokemonCard> = PublishRelay.create()

    private lateinit var adapter: DeckBuilderPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)

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
            val superType = when(tabs.selectedTabPosition) {
                0 -> SuperType.POKEMON
                1 -> SuperType.TRAINER
                2 -> SuperType.ENERGY
                else -> SuperType.POKEMON
            }
            val intent = SearchActivity.createIntent(this, superType)
            startActivityForResult(intent, SearchActivity.RC_PICK_CARD)
        }

        dropZone.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    v.animate()
                            .translationY(0f)
                            .setDuration(150L)
                            .setInterpolator(FastOutLinearInInterpolator())
                            .start()
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.setBackgroundColor(color(R.color.dropzone_red_selected))
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    v.setBackgroundColor(color(R.color.dropzone_red))
                }
                DragEvent.ACTION_DROP -> {
                    val localState = event.localState
                    if (localState is PokemonCardView) {
                        localState.card?.let {
                            removePokemon.accept(it)
                        }
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.setBackgroundColor(color(R.color.dropzone_red))
                    v.animate()
                            .translationY(-dpToPx(104f))
                            .setDuration(150L)
                            .setInterpolator(FastOutLinearInInterpolator())
                            .start()
                }
            }
            true
        }

        slidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                Timber.d("onPanelSlide($slideOffset)")
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {

            }
        })


        renderer.start()
        presenter.start()
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


    override fun addCards(): Observable<List<PokemonCard>> {
        return addPokemon
    }


    override fun removeCard(): Observable<PokemonCard> {
        return removePokemon
    }


    override fun editDeckName(): Observable<String> {
        return Observable.empty()
    }


    override fun editDeckDescription(): Observable<String> {
        return Observable.empty()
    }


    override fun showPokemonCards(cards: List<StackedPokemonCard>) {
        adapter.setCards(SuperType.POKEMON, cards)
        cardCount.countStacks(SuperType.POKEMON, cards)
    }


    override fun showTrainerCards(cards: List<StackedPokemonCard>) {
        adapter.setCards(SuperType.TRAINER, cards)
        cardCount.countStacks(SuperType.TRAINER, cards)
    }


    override fun showEnergyCards(cards: List<StackedPokemonCard>) {
        adapter.setCards(SuperType.ENERGY, cards)
        cardCount.countStacks(SuperType.ENERGY, cards)
    }


    override fun showDeckName(name: String) {

    }


    override fun showDeckDescription(description: String) {

    }


    companion object {
        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
    }
}