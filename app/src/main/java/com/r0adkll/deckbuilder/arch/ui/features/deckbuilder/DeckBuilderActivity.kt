package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter.DeckBuilderPagerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.arch.ui.features.exporter.DeckExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindOptionalParcelable
import com.r0adkll.deckbuilder.util.extensions.isVisible
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.uiDebounce
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.*
import gov.scstatehouse.houseofcards.util.ImeUtils
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_deck_builder.*
import javax.inject.Inject


class DeckBuilderActivity : BaseActivity(), DeckBuilderUi, DeckBuilderUi.Intentions, DeckBuilderUi.Actions{

    private val deck: Deck? by bindOptionalParcelable(EXTRA_DECK)

    @State
    override var state: DeckBuilderUi.State = DeckBuilderUi.State.DEFAULT

    @Inject lateinit var renderer: DeckBuilderRenderer
    @Inject lateinit var presenter: DeckBuilderPresenter

    private val pokemonCardClicks: Relay<PokemonCardView> = PublishRelay.create()
    private val addPokemon: Relay<List<PokemonCard>> = PublishRelay.create()
    private val removePokemon: Relay<PokemonCard> = PublishRelay.create()
    private val saveDeck: Relay<Unit> = PublishRelay.create()
    private val countPadding: Float by lazy { dpToPx(16f) }
    private val countPaddingTop: Float by lazy { dpToPx(16f) }
    private val formatPaddingTop: Float by lazy { dpToPx(8f) }
    private val panelOffsetTop: Float by lazy { dpToPx(48f) }

    private lateinit var adapter: DeckBuilderPagerAdapter
    private var savingSnackBar: Snackbar? = null


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
            val intent = SearchActivity.createIntent(this, superType, state.allCards)
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
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                interpolateCardCounter(panel, slideOffset)

                val infoBarOffset = calculateAlpha(slideOffset, .95f)
                infoBar.alpha = infoBarOffset
                infoBar.elevation = infoBarOffset * dpToPx(4f)
                text_input_deck_name.alpha = calculateAlpha(slideOffset, .80f)
                text_input_deck_description.alpha = calculateAlpha(slideOffset, .65f)
//                format_expanded.alpha = 1f - (slideOffset * 9f).coerceAtMost(1f)
//                format_standard.alpha = 1f - (slideOffset * 9f).coerceAtMost(1f)
//                formatStandardDetail.alpha = calculateAlpha(slideOffset, .40f)
//                formatExpandedDetail.alpha = calculateAlpha(slideOffset, .40f)

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

            private fun calculateAlpha(offset: Float, ratio: Float): Float = (offset - (1 - ratio)).coerceAtLeast(0f) / ratio


            private fun interpolateCardCounter(panel: View, offset: Float) {
                val bottom = if (formatExpandedDetail.isVisible()){
                    formatExpandedDetail.bottom
                } else if (formatStandardDetail.isVisible()) {
                    formatStandardDetail.bottom
                } else {
                    text_input_deck_description.bottom
                }

                val distance = ((bottom - cardCount.top) + countPaddingTop)
                val distanceYRatio = distance / (panel.height - panelOffsetTop)
                val distanceX = (cardCount.left - countPadding)

                val cardOffset = offset.coerceAtMost(distanceYRatio) / distanceYRatio
                val translationY = distance * cardOffset
                val translationX = distanceX * cardOffset
                cardCount.translationY = translationY
                cardCount.translationX = -translationX
            }


        })

        infoBar.setNavigationOnClickListener {
            slidingLayout.panelState = COLLAPSED
        }

        if (state.deck == null && deck != null) {
            state = state.copy(
                    deck = deck,
                    pokemonCards = deck!!.cards.filter { it.supertype == SuperType.POKEMON },
                    trainerCards = deck!!.cards.filter { it.supertype == SuperType.TRAINER },
                    energyCards = deck!!.cards.filter { it.supertype == SuperType.ENERGY },
                    name = deck?.name,
                    description = deck?.description
            )
        }

        renderer.start()
        presenter.start()


        disposables += pokemonCardClicks
                .subscribe {
                    CardDetailActivity.show(this, it)
                }
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
        saveItem.isVisible = state.hasChanged && !state.isSaving
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_export -> {
                val exportDeck = Deck("", "", "", state.allCards, 0L)
                startActivity(DeckExportActivity.createIntent(this, exportDeck))
                true
            }
            R.id.action_save -> {
                saveDeck.accept(Unit)
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


    override fun saveDeck(): Observable<Unit> {
        return saveDeck
    }


    override fun showSaveAction(hasChanges: Boolean) {
        supportInvalidateOptionsMenu()
    }


    override fun showCardCount(count: Int) {
        cardCount.totalCount(count)
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
        if(name.isBlank()) {
            appbar?.setTitle(R.string.deckbuilder_default_title)
        }
        else {
            appbar?.title = name
        }
        if (inputDeckName.text.isBlank()) {
            inputDeckName.setText(name)
            inputDeckName.setSelection(name.length)
        }
    }


    override fun showDeckDescription(description: String) {
        if (inputDeckDescription.text.isBlank()) {
            inputDeckDescription.setText(description)
            inputDeckDescription.setSelection(description.length)
        }
    }


    override fun showIsSaving(isSaving: Boolean) {
        supportInvalidateOptionsMenu()
        if (isSaving) {
            if (savingSnackBar == null) {
                savingSnackBar = Snackbar.make(pager, R.string.deckbuilder_saving_message, Snackbar.LENGTH_INDEFINITE)
            }
            else {
                savingSnackBar?.setText(R.string.deckbuilder_saving_message)
                savingSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
            }

            savingSnackBar?.show()
        }
        else {
            if (savingSnackBar == null) {
                savingSnackBar = Snackbar.make(pager, R.string.deckbuilder_saved_message, Snackbar.LENGTH_SHORT)
            }
            else {
                savingSnackBar?.setText(R.string.deckbuilder_saved_message)
                savingSnackBar?.duration = Snackbar.LENGTH_SHORT
            }

            if (savingSnackBar?.isShown == true) {
                savingSnackBar?.show()
            }
        }
    }


    override fun showIsStandard(isStandard: Boolean) {
//        format_standard.setVisible(isStandard)
    }


    override fun showIsExpanded(isExpanded: Boolean) {
//        format_expanded.setVisible(isExpanded)
    }


    companion object {
        @JvmField val EXTRA_DECK = "com.r0adkll.deckbuilder.intent.EXTRA_DECK"

        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
        fun createIntent(context: Context, deck: Deck): Intent {
            val intent = createIntent(context)
            intent.putExtra(EXTRA_DECK, deck)
            return intent
        }
    }
}