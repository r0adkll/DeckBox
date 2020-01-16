package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.evernote.android.state.State
import com.ftinc.kit.arch.di.HasComponent
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.arch.util.uiDebounce
import com.ftinc.kit.extensions.dimenPixelSize
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.extensions.snackbar
import com.ftinc.kit.util.bindBoolean
import com.ftinc.kit.util.bindString
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.textChanges
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.FlagPreferences
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.components.drag.EditDragListener
import com.r0adkll.deckbuilder.arch.ui.components.drag.TabletDragListener
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.DeckImagePickerFragment
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderComponent
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.SessionModule
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.erroradapter.RuleRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter.DeckBuilderPagerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.exporter.MultiExportActivity
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportActivity
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchActivity
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingActivity
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.MarketplaceHelper
import com.r0adkll.deckbuilder.util.extensions.formatPrice
import com.r0adkll.deckbuilder.util.extensions.margins
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_deck_builder.*
import kotlinx.android.synthetic.main.layout_detail_panel.*
import kotlinx.android.synthetic.main.layout_marketplace.*
import timber.log.Timber
import javax.inject.Inject

class DeckBuilderActivity : BaseActivity(),
    HasComponent<DeckBuilderComponent>,
    DeckBuilderUi,
    DeckBuilderUi.Intentions,
    DeckBuilderUi.Actions {

    @Suppress("MagicNumber")
    inner class DeckBuilderPanelSlideListener : SlidingUpPanelLayout.PanelSlideListener {
        private val defaultOffset: Float by lazy { dp(22f) }

        override fun onPanelSlide(panel: View, slideOffset: Float) {
            interpolateBottomBar(panel, slideOffset)
            interpolatePanelIndicator(slideOffset)
            interpolateErrorMarker(panel, slideOffset)

            val infoBarOffset = calculateAlpha(slideOffset, .95f)
            infoBar.alpha = infoBarOffset
            infoBar.elevation = infoBarOffset * dp(4f)
            deckImage.alpha = calculateAlpha(slideOffset, .80f)
            text_input_deck_name.alpha = calculateAlpha(slideOffset, .80f)
            text_input_deck_description.alpha = calculateAlpha(slideOffset, .65f)

            if (slideOffset > 0f && !infoBar.isVisible) {
                infoBar.isVisible = true
            } else if (slideOffset == 0f && infoBar.isVisible) {
                infoBar.isInvisible = true
            }
        }

        override fun onPanelStateChanged(
            panel: View,
            previousState: SlidingUpPanelLayout.PanelState,
            newState: SlidingUpPanelLayout.PanelState
        ) {
            if (previousState != COLLAPSED && newState == COLLAPSED) {
                imm.hideSoftInputFromWindow(inputDeckName.windowToken, 0)
                imm.hideSoftInputFromWindow(inputDeckDescription.windowToken, 0)
            }
        }

        private fun calculateAlpha(offset: Float, ratio: Float): Float {
            return (offset - (1 - ratio)).coerceAtLeast(0f) / ratio
        }

        private fun interpolateErrorMarker(panel: View, offset: Float) {
            if (ruleAdapter.itemCount > 0) {
                val iconOffset = ruleRecycler.getChildAt(0)?.let {
                    (it.height.toFloat() / 2f)
                } ?: defaultOffset
                val recyclerOffset = 1 - ((ruleRecycler.height.toFloat() - iconOffset) / panel.height.toFloat())
                deckError.isVisible = offset < recyclerOffset
            }
        }

        private fun interpolateBottomBar(panel: View, offset: Float) {
            infoBottomBar.translationY = offset * (panel.height - infoBottomBar.height)
        }

        private fun interpolatePanelIndicator(offset: Float) {
            val transY = mainContent.height * offset
            panelIndicator.translationY = -transY
            panelIndicator.alpha = (1f - (offset * 5f).coerceAtMost(1f)) * .54f
        }
    }

    private val deckId by bindString(EXTRA_DECK_ID)
    private val isNewDeck by bindBoolean(EXTRA_IS_NEW)

    private val imm: InputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    @State override var state: DeckBuilderUi.State = DeckBuilderUi.State.DEFAULT

    @Inject lateinit var renderer: DeckBuilderRenderer
    @Inject lateinit var presenter: DeckBuilderPresenter
    @Inject lateinit var editRepository: EditRepository
    @Inject lateinit var flags: FlagPreferences
    @Inject lateinit var remote: Remote

    private val pokemonCardClicks: Relay<PokemonCardView> = PublishRelay.create()
    private val editCardIntentions: EditCardIntentions = EditCardIntentions()
    private val editDeckClicks: Relay<Boolean> = PublishRelay.create()
    private val editOverviewClicks: Relay<Boolean> = PublishRelay.create()

    private lateinit var component: DeckBuilderComponent
    private lateinit var adapter: DeckBuilderPagerAdapter
    private lateinit var ruleAdapter: RuleRecyclerAdapter
    private val panelSlideListener = DeckBuilderPanelSlideListener()
    private var pendingImport: List<PokemonCard>? = null

    private val fabClickListener = View.OnClickListener {
        if (fragmentSwitcher == null) {
            Analytics.event(Event.SelectContent.Action("add_new_card"))
            startActivityForResult(SearchActivity.createIntent(this, state.deckId), RC_EDIT_CHANGES)
        } else {
            // Show the overview fragment
            editOverviewClicks.accept(true)
        }
    }

    private val infoBarClickListener = View.OnClickListener {
        var normalOperation = true
        if (fragmentSwitcher != null) {
            if (fragmentSwitcher!!.displayedChild == 1 /* Overview */) {
                editOverviewClicks.accept(false)
                normalOperation = false
            }
        }

        if (normalOperation) {
            imm.hideSoftInputFromWindow(inputDeckName.windowToken, 0)
            imm.hideSoftInputFromWindow(inputDeckDescription.windowToken, 0)
            slidingLayout.panelState = COLLAPSED
        }
    }

    @SuppressLint("RxSubscribeOnError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)

        state = state.copy(deckId = deckId)

        // Setup AppBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        appbarTitle.setOnClickListener {
            slidingLayout.panelState = EXPANDED
            inputDeckName.requestFocus()
            imm.showSoftInput(inputDeckName, 0)
        }
        appbar?.setNavigationOnClickListener {
            supportFinishAfterTransition()
        }

        // Setup pager
        ruleAdapter = RuleRecyclerAdapter(this)
        ruleRecycler.layoutManager = LinearLayoutManager(this)
        ruleRecycler.adapter = ruleAdapter

        adapter = DeckBuilderPagerAdapter(this, pokemonCardClicks, editCardIntentions)
        pager.adapter = adapter
        pager.offscreenPageLimit = OFFSCREEN_PAGE_LIMIT
        tabs.setupWithViewPager(pager)

        fab.setOnClickListener(fabClickListener)
        slidingLayout.addPanelSlideListener(panelSlideListener)
        infoBar.setNavigationOnClickListener(infoBarClickListener)

        tabletDropZone?.let {
            TabletDragListener.attach(it, pager) { card ->
                editCardIntentions.addCardClicks.accept(listOf(card))
            }
        }

        EditDragListener.attach(dropZone, object : EditDragListener.DropListener {

            override fun onAddCard(card: PokemonCard) {
                editCardIntentions.addCardClicks.accept(listOf(card))
            }

            override fun onRemoveCard(card: PokemonCard) {
                editCardIntentions.removeCardClicks.accept(card)
            }
        })

        disposables += pokemonCardClicks
            .uiDebounce()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Analytics.event(Event.SelectContent.PokemonCard(it.card?.id ?: "unknown"))
                CardDetailActivity.show(this, it, state.deckId)
            }

        disposables += actionDeckImage.clicks()
            .uiDebounce()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                DeckImagePickerFragment.newInstance(state.deckId, state.image)
                    .show(supportFragmentManager, DeckImagePickerFragment.TAG)
            }

        actionView.isVisible = remote.marketplaceMassEntryEnabled
        actionView.setOnClickListener {
            val link = MarketplaceHelper.buildAffiliateLink(state.allCards, state.products)
            MarketplaceHelper.openLink(this, link)
        }

        priceMarketLayout.setOnClickListener {
            Analytics.event(Event.SelectContent.Action("market_price_info"))
            MarketplaceHelper.showMarketPriceExplanationDialog(this)
        }

        if (slidingLayout.panelState != COLLAPSED) {
            slidingLayout.panelState = COLLAPSED
        }
    }

    override fun getComponent(): DeckBuilderComponent {
        return component
    }

    override fun setupComponent() {
        this.component = DeckApp.component.deckBuilderComponentBuilder()
            .sessionModule(SessionModule(deckId))
            .deckBuilderModule(DeckBuilderModule(this))
            .build()
        this.component.inject(this)

        delegates += StatefulActivityDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulActivityDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun onStart() {
        super.onStart()
        if (pendingImport != null) {
            editCardIntentions.addCardClicks.accept(pendingImport)
            pendingImport = null
        }
    }

    override fun onBackPressed() {
        if (fragmentSwitcher != null && fragmentSwitcher!!.displayedChild == 1) {
            editOverviewClicks.accept(false)
        } else if (slidingLayout.panelState == EXPANDED) {
            slidingLayout.panelState = COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.i("onActivityResult($requestCode, $resultCode)")

        val importResult = DeckImportActivity.parseResults(resultCode, requestCode, data)
        importResult?.let {
            Analytics.event(Event.SelectContent.Action("import_cards"))
            editCardIntentions.addCardClicks.accept(it)

            // if the lifecycle isn't at a point where the presenter isn't started, store for when it is
            if (!lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                pendingImport = it
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_deck_builder, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val editItem = menu.findItem(R.id.action_edit)
        val finishEditItem = menu.findItem(R.id.action_finish_edit)
        editItem.isVisible = !state.isEditing
        finishEditItem.isVisible = state.isEditing

        val testItem = menu.findItem(R.id.action_test)
        testItem.isVisible = BuildConfig.DEBUG
        testItem.isEnabled = state.validation.isValid
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                editDeckClicks.accept(true)
                true
            }
            R.id.action_finish_edit -> {
                editDeckClicks.accept(false)
                true
            }
            R.id.action_import -> {
                Analytics.event(Event.SelectContent.MenuAction("import_decklist"))
                DeckImportActivity.show(this)
                true
            }
            R.id.action_export -> {
                Analytics.event(Event.SelectContent.MenuAction("export_decklist"))
                startActivity(MultiExportActivity.createIntent(this, state.deckId))
                true
            }
            R.id.action_test -> {
                Analytics.event(Event.SelectContent.MenuAction("test_deck"))
                startActivity(DeckTestingActivity.createIntent(this, state.deckId))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun render(state: DeckBuilderUi.State) {
        this.state = state
        renderer.render(state)
    }

    override fun addCards(): Observable<List<PokemonCard>> {
        return editCardIntentions.addCardClicks
            .doOnNext { Analytics.event(Event.SelectContent.Action("edit_add_card")) }
    }

    override fun removeCard(): Observable<PokemonCard> {
        return editCardIntentions.removeCardClicks
            .doOnNext { Analytics.event(Event.SelectContent.Action("edit_remove_card")) }
    }

    override fun editDeckClicks(): Observable<Boolean> {
        return editDeckClicks
    }

    override fun editOverviewClicks(): Observable<Boolean> {
        return editOverviewClicks
    }

    override fun editDeckName(): Observable<String> {
        return inputDeckName.textChanges()
            .skipInitialValue()
            .map { it.toString() }
            .uiDebounce(DEBOUNCE_TIME_MS)
            .doOnNext {
                Analytics.event(Event.SelectContent.Deck.EditName)
            }
    }

    override fun editDeckDescription(): Observable<String> {
        return inputDeckDescription.textChanges()
            .skipInitialValue()
            .map { it.toString() }
            .uiDebounce(DEBOUNCE_TIME_MS)
            .doOnNext {
                Analytics.event(Event.SelectContent.Deck.EditDescription)
            }
    }

    override fun editDeckCollectionOnly(): Observable<Boolean> {
        return collectionSwitch.checkedChanges()
            .skipInitialValue()
            .doOnNext {
                Analytics.event(Event.SelectContent.Deck.EditCollectionOnly(it))
            }
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
        if (name.isBlank()) {
            appbarTitle?.setText(if (isNewDeck) R.string.deckbuilder_default_title else R.string.deckbuilder_edit_title)
        } else {
            appbarTitle?.text = name
        }
        if (inputDeckName.text.isNullOrBlank()) {
            inputDeckName.setText(name)
            inputDeckName.setSelection(name.length)
        }
    }

    override fun showDeckDescription(description: String) {
        if (inputDeckDescription.text.isNullOrBlank()) {
            inputDeckDescription.setText(description)
            inputDeckDescription.setSelection(description.length)
        }
    }

    override fun showDeckImage(image: DeckImage?) {
        when (image) {
            null -> {
                deckImage.clear()
                deckImage.setImageResource(R.color.grey_300)
            }
            is DeckImage.Pokemon -> {
                deckImage.primaryType = null
                deckImage.secondaryType = null
                GlideApp.with(this)
                    .load(image.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(deckImage)
            }
            is DeckImage.Type -> {
                deckImage.primaryType = image.type1
                deckImage.secondaryType = image.type2
                deckImage.invalidate()
            }
        }
    }

    override fun showDeckCollectionOnly(collectionOnly: Boolean) {
        adapter.isCollectionEnabled = collectionOnly
        if (collectionSwitch.isChecked != collectionOnly) {
            collectionSwitch.isChecked = collectionOnly
        }
    }

    override fun showPrices(low: Double?, market: Double?, high: Double?) {
        val isVisible = low != null || market != null || high != null
        divider.margins(top = if (isVisible) {
            dimenPixelSize(R.dimen.margin_small)
        } else {
            dimenPixelSize(R.dimen.margin_tiny)
        })
        costsLayout.isVisible = isVisible
        priceLow.text = low?.formatPrice() ?: "n/a"
        priceMarket.text = market?.formatPrice() ?: "n/a"
        priceHigh.text = high?.formatPrice() ?: "n/a"
    }

    override fun showCollectionPrices(low: Double?, market: Double?, high: Double?) {
        val isVisible = low != null || market != null || high != null
        collectionPriceDivider.isVisible = isVisible
        collectionPricesRow.isVisible = isVisible
        collectionPriceLow.text = low?.times(-1.0)?.formatPrice() ?: "n/a"
        collectionPriceMarket.text = market?.times(-1.0)?.formatPrice() ?: "n/a"
        collectionPriceHigh.text = high?.times(-1.0)?.formatPrice() ?: "n/a"
    }

    override fun showIsEditing(isEditing: Boolean) {
        invalidateOptionsMenu()
        adapter.isEditing = isEditing
    }

    override fun showIsOverview(isOverview: Boolean) {
        if (fragmentSwitcher != null) {
            if (isOverview && fragmentSwitcher!!.displayedChild == 0) {
                // Show the overview fragment
                fragmentSwitcher?.setInAnimation(this, R.anim.slide_in_left)
                fragmentSwitcher?.setOutAnimation(this, R.anim.slide_out_right)
                fragmentSwitcher?.showNext()

                if (slidingLayout.panelState == EXPANDED) {
                    panelSlideListener.onPanelSlide(slidingLayout, 1f)
                }

                slidingLayout.panelState = EXPANDED
                slidingLayout.isTouchEnabled = false
                Analytics.event(Event.SelectContent.Action("open_overview"))
            } else if (!isOverview && fragmentSwitcher!!.displayedChild == 1) {
                fragmentSwitcher?.setInAnimation(this, R.anim.slide_in_right)
                fragmentSwitcher?.setOutAnimation(this, R.anim.slide_out_left)
                fragmentSwitcher!!.showPrevious()
                slidingLayout.panelState = COLLAPSED
                slidingLayout.isTouchEnabled = true
                Analytics.event(Event.SelectContent.Action("close_overview"))
            }
        }
    }

    override fun showError(description: String) {
        snackbar(description)
    }

    override fun showFormat(format: Format) {
        deckFormat.text = format.name.toLowerCase().capitalize()
    }

    override fun showBrokenRules(errors: List<Int>) {
        deckError.isVisible = errors.isNotEmpty()
        ruleAdapter.submitList(errors)
    }

    companion object {
        private const val EXTRA_IS_NEW = "DeckBuilderActivity.IsNew"
        private const val EXTRA_DECK_ID = "DeckBuilderActivity.DeckId"
        private const val OFFSCREEN_PAGE_LIMIT = 3
        private const val RC_EDIT_CHANGES = 1000
        private const val DEBOUNCE_TIME_MS = 1000L

        private fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)

        fun createIntent(context: Context, deckId: String, isNew: Boolean = false): Intent {
            val intent = createIntent(context)
            intent.putExtra(EXTRA_DECK_ID, deckId)
            intent.putExtra(EXTRA_IS_NEW, isNew)
            return intent
        }
    }
}
