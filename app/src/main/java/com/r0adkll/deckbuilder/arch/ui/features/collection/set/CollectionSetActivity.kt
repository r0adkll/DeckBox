package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.smallestWidth
import com.ftinc.kit.util.ScreenUtils
import com.ftinc.kit.util.bindParcelable
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.palette.ExpansionPaletteAction
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.adapter.CollectionSetRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.di.CollectionSetModule
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.extensions.layoutHeight
import com.r0adkll.deckbuilder.util.extensions.loadOfflineUri
import com.r0adkll.deckbuilder.util.extensions.margins
import com.r0adkll.deckbuilder.util.extensions.readablePercentage
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapViewTarget
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_collection_set.*
import javax.inject.Inject

class CollectionSetActivity : BaseActivity(), CollectionSetUi, CollectionSetUi.Intentions, CollectionSetUi.Actions {

    private val expansion: Expansion by bindParcelable(EXTRA_EXPANSION)

    override var state: State = State.DEFAULT

    @Inject lateinit var presenter: CollectionSetPresenter
    @Inject lateinit var renderer: CollectionSetRenderer

    private val addCardClicks = PublishRelay.create<List<PokemonCard>>()
    private val removeCardClicks = PublishRelay.create<PokemonCard>()
    private val incrementSetClicks = PublishRelay.create<Unit>()
    private val toggleMissingCardsClicks = PublishRelay.create<Unit>()

    private lateinit var adapter: CollectionSetRecyclerAdapter
    private var statusBarHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_set)

        GlideApp.with(this)
            .`as`(PaletteBitmap::class.java)
            .loadOfflineUri(this, expansion.logoUrl)
            .into(PaletteBitmapViewTarget(logo, listOf(
                ExpansionPaletteAction(backdrop, expansion, contrastListener = this::setNavigationColor)
            )))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        appbar?.setNavigationOnClickListener { finish() }

        adapter = CollectionSetRecyclerAdapter(
            this,
            removeCardClicks,
            addCardClicks,
            {
                addCardClicks.accept(listOf(it.card))
            },
            { _, stackedPokemonCard ->
                Analytics.event(Event.SelectContent.PokemonCard(stackedPokemonCard.card.id))
                startActivity(CardDetailActivity.createIntent(this, stackedPokemonCard.card))
                true
            }
        )
        adapter.emptyView = emptyView

        val spanCount = if (smallestWidth(ScreenUtils.Config.TABLET_10)) {
            TABLET_SPAN_COUNT
        } else {
            PHONE_SPAN_COUNT
        }
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, spanCount)
        (recycler.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        @Suppress("MagicNumber")
        appbar?.doOnApplyWindowInsets { _, insets, _ ->
            statusBarHeight = insets.systemWindowInsetTop
            appbar?.margins(top = statusBarHeight)
            logo?.layoutHeight(dip(100f))
            logo?.margins(top = statusBarHeight + dip(16f))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_collection_set, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val size = appbar?.menu?.size() ?: 0
        (0 until size).forEach {
            appbar?.menu?.getItem(it)?.let { item ->
                MenuItemCompat.setIconTintList(item, ColorStateList.valueOf(progressBar.borderColor))
            }
        }
        return menu?.findItem(R.id.action_toggle_missing_cards)?.let { toggleMissingCards ->
            toggleMissingCards.isChecked = state.onlyMissingCards
            toggleMissingCards.setIcon(if (state.onlyMissingCards) {
                R.drawable.toggle_switch
            } else {
                R.drawable.toggle_switch_off
            })
            true
        } ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_all -> {
                Analytics.event(Event.SelectContent.MenuAction("collection_add_all"))
                incrementSetClicks.accept(Unit)
                true
            }
            R.id.action_toggle_missing_cards -> {
                Analytics.event(Event.SelectContent.MenuAction("toggle_missing_cards"))
                toggleMissingCardsClicks.accept(Unit)
                true
            }
            else -> false
        }
    }

    override fun setupComponent() {
        DeckApp.component.plus(CollectionSetModule(this))
            .inject(this)

        // Force update state from intent extras
        state = state.copy(expansion = expansion)

        delegates += StatefulActivityDelegate(renderer)
        delegates += StatefulActivityDelegate(presenter)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun addCard(): Observable<List<PokemonCard>> {
        return addCardClicks
            .doOnNext {
                it.forEach { card ->
                    Analytics.event(Event.SelectContent.Collection.Increment(card.id))
                }
            }
    }

    override fun removeCard(): Observable<PokemonCard> {
        return removeCardClicks
            .doOnNext {
                Analytics.event(Event.SelectContent.Collection.Decrement(it.id))
            }
    }

    override fun addSet(): Observable<Unit> {
        return incrementSetClicks
    }

    override fun toggleMissingCards(): Observable<Unit> {
        return toggleMissingCardsClicks
    }

    override fun showOverallProgress(progress: Float) {
        progressBar.progress = progress
        progressCompletion.text = getString(R.string.completion_format, progress.readablePercentage)
    }

    override fun showCollection(cards: List<StackedPokemonCard>) {
        adapter.submitList(cards)
    }

    override fun showOnlyMissingCards(visible: Boolean) {
        invalidateOptionsMenu()
    }

    override fun hideError() {
        emptyView.setMessage(R.string.empty_collection_set)
    }

    override fun showError(description: String) {
        emptyView.message = description
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.state = if (isLoading) {
            EmptyView.State.LOADING
        } else {
            EmptyView.State.EMPTY
        }
    }

    private fun setNavigationColor(isLight: Boolean) {
        val color = if (isLight) color(R.color.black87) else Color.WHITE
        val secondaryColor = if (isLight) color(R.color.black54) else color(R.color.white50)
        appbar?.navigationIcon?.setTint(color)
        progressCompletion.setTextColor(color)
        progressBar.borderColor = color
        progressBar.trackColor = secondaryColor
        val size = appbar?.menu?.size() ?: 0
        (0 until size).forEach {
            appbar?.menu?.getItem(it)?.let { item ->
                MenuItemCompat.setIconTintList(item, ColorStateList.valueOf(color))
            }
        }
    }

    companion object {
        private const val EXTRA_EXPANSION = "CollectionSetActivity.Expansion"
        const val TABLET_SPAN_COUNT = 9
        const val PHONE_SPAN_COUNT = 3

        fun createIntent(context: Context, expansion: Expansion): Intent {
            val intent = Intent(context, CollectionSetActivity::class.java)
            intent.putExtra(EXTRA_EXPANSION, expansion)
            return intent
        }
    }
}
