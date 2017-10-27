package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v7.app.AlertDialog
import android.view.DragEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.evernote.android.state.State
import com.ftinc.kit.kotlin.extensions.*
import com.jakewharton.rxbinding2.widget.textChanges
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
import com.r0adkll.deckbuilder.util.extensions.isVisible
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.*
import gov.scstatehouse.houseofcards.util.ImeUtils
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_builder.*
import kotlinx.android.synthetic.main.activity_deck_builder.view.*
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
            if (state.hasChanged) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.deckbuilder_unsaved_changes_title)
                        .setMessage(R.string.deckbuilder_unsaved_changes_message)
                        .setPositiveButton(R.string.dialog_action_yes, { dialog, _ ->
                            dialog.dismiss()
                            supportFinishAfterTransition()
                        })
                        .setNegativeButton(R.string.dialog_action_no, { dialog, _ -> dialog.dismiss() })
                        .show()
            }
            else {
                supportFinishAfterTransition()
            }
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
                val infoBarOffset = calculateInfoBarAlpha(slideOffset)
                infoBar.alpha = infoBarOffset
                infoBar.elevation = infoBarOffset * dpToPx(4f)

                if (slideOffset > 0f && !infoBar.isVisible()) {
                    infoBar.visible()
                }
                else if (slideOffset == 0f && infoBar.isVisible()) {
                    infoBar.invisible()
                }
            }

            override fun onPanelStateChanged(panel: View, previousState: SlidingUpPanelLayout.PanelState, newState: SlidingUpPanelLayout.PanelState) {
                if (previousState != COLLAPSED && newState == COLLAPSED) {
                    ImeUtils.hideIme(panel)
                }
            }

            private fun calculateInfoBarAlpha(offset: Float): Float = (offset - .75f).coerceAtLeast(0f) / .25f
        })

        infoBar.setNavigationOnClickListener {
            slidingLayout.panelState = COLLAPSED
        }


        renderer.start()
        presenter.start()
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun onBackPressed() {
        if (state.hasChanged) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.deckbuilder_unsaved_changes_title)
                    .setMessage(R.string.deckbuilder_unsaved_changes_message)
                    .setPositiveButton(R.string.dialog_action_yes, { dialog, _ ->
                        dialog.dismiss()
                        super.onBackPressed()
                    })
                    .setNegativeButton(R.string.dialog_action_no, { dialog, _ -> dialog.dismiss() })
                    .show()
        }
        else {
            super.onBackPressed()
        }
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


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val saveItem = menu.findItem(R.id.action_save)
        saveItem.isVisible = state.hasChanged
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
        return inputDeckName.textChanges()
                .map { it.toString() }
                .uiDebounce()
    }


    override fun editDeckDescription(): Observable<String> {
        return inputDeckDescription.textChanges()
                .map { it.toString() }
                .uiDebounce()
    }


    override fun showSaveAction(hasChanges: Boolean) {
        supportInvalidateOptionsMenu()
    }


    override fun showCardCount(count: Int) {
        cardCounter.setVisible(count > 0)
        cardCounter.text = count.toString()
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
        if(name.isNullOrBlank()) {
            appbar?.setTitle(R.string.deckbuilder_default_title)
        }
        else {
            appbar?.title = name
        }
        inputDeckName.setText(name)
        inputDeckName.setSelection(name.length)
    }


    override fun showDeckDescription(description: String) {
        inputDeckDescription.setText(description)
        inputDeckDescription.setSelection(description.length)
    }


    companion object {
        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
    }
}