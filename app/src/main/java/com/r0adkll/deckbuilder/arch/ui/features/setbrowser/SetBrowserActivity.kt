package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.util.bindParcelable
import com.ftinc.kit.widget.EmptyView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.components.palette.ExpansionPaletteAction
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonBuilderRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonItem
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.State
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.di.SetBrowserModule
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.ScreenUtils.Config.TABLET_10
import com.r0adkll.deckbuilder.util.ScreenUtils.smallestWidth
import com.r0adkll.deckbuilder.util.extensions.addLayoutHeight
import com.r0adkll.deckbuilder.util.extensions.layoutHeight
import com.r0adkll.deckbuilder.util.extensions.loadOfflineUri
import com.r0adkll.deckbuilder.util.extensions.margins
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapViewTarget
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_set_browser.*
import javax.inject.Inject

class SetBrowserActivity : BaseActivity(), SetBrowserUi, SetBrowserUi.Intentions, SetBrowserUi.Actions {

    private val expansion: Expansion by bindParcelable(EXTRA_EXPANSION)

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: SetBrowserRenderer
    @Inject lateinit var presenter: SetBrowserPresenter

    private val filterChanges: Relay<BrowseFilter> = PublishRelay.create()
    private val cardClicks: Relay<PokemonCardView> = PublishRelay.create()
    private lateinit var adapter: PokemonBuilderRecyclerAdapter

    private var statusBarHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_browser)

        @Suppress("MagicNumber")
        state = state.copy(
            setCode = expansion.code,
            pageSize = expansion.totalCards + 100 /* Hack to account for secret rares */
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        appbar?.setNavigationOnClickListener { finish() }

        GlideApp.with(this)
            .`as`(PaletteBitmap::class.java)
            .loadOfflineUri(this, expansion.logoUrl)
            .into(PaletteBitmapViewTarget(logo, listOf(
                ExpansionPaletteAction(backdrop, expansion, contrastListener = this::setNavigationColor)
            )))

        tabs.tabMode = TabLayout.MODE_AUTO
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val filter = tab.tag as? BrowseFilter
                filterChanges.accept(filter)
                Analytics.event(Event.SelectContent.Action("expansion_filter", filter.toString()))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        @Suppress("MagicNumber")
        appBarLayout.doOnApplyWindowInsets { _, insets, _ ->
            statusBarHeight = insets.systemWindowInsetTop
            appBarLayout.addLayoutHeight(statusBarHeight - dip(24f))
            appbar?.margins(top = statusBarHeight)
            logo?.layoutHeight(dip(100f))
            logo?.margins(top = (statusBarHeight - dip(24f)) / 2)
        }

        @Suppress("MagicNumber")
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { view, offset ->
            val height = view.height.toFloat() - ((appbar?.height ?: 0) + statusBarHeight)
            val percent = offset.toFloat() / height
            val minScale = 0.4f
            val maxScale = 1f
            val scale = maxScale + ((maxScale - minScale) * percent)
            logo.pivotX = logo.width * 0.5f
            logo.pivotY = logo.height.toFloat() * 0.9f
            logo.scaleX = scale
            logo.scaleY = scale
        })

        @SuppressLint("RxSubscribeOnError")
        disposables += cardClicks
            .subscribe {
                Analytics.event(Event.SelectContent.PokemonCard(it.card?.id ?: "unknown"))
                CardDetailActivity.show(this, it)
            }

        val spanCount = if (smallestWidth(TABLET_10)) {
            CollectionSetActivity.TABLET_SPAN_COUNT
        } else {
            CollectionSetActivity.PHONE_SPAN_COUNT
        }
        adapter = PokemonBuilderRecyclerAdapter(this, spanCount, EditCardIntentions(), cardClicks)
        adapter.emptyView = emptyView
        recycler.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, spanCount)
        recycler.adapter = adapter
    }

    override fun setupComponent() {
        DeckApp.component
            .plus(SetBrowserModule(this))
            .inject(this)

        delegates += StatefulActivityDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulActivityDelegate(presenter, Lifecycle.Event.ON_START)
    }

    override fun render(state: State) {
        this.state = state
        renderer.render(state)
    }

    override fun filterChanged(): Observable<BrowseFilter> {
        return filterChanges
    }

    override fun setFilter(filter: BrowseFilter) {
        for (i in (0 until tabs.tabCount)) {
            val tab = tabs.getTabAt(i)
            val tag = tab?.tag
            if (tag == filter) {
                tab.select()
                break
            }
        }
    }

    override fun setFilters(filters: List<BrowseFilter>) {
        tabs.removeAllTabs()
        filters.forEach { filter ->
            val tab = tabs.newTab()
            tab.setText(filter.title)
            tab.tag = filter
            tabs.addTab(tab)
        }
    }

    override fun setCards(cards: List<PokemonCard>) {
        adapter.submitList(cards.map { PokemonItem.Single(StackedPokemonCard(it)) })
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.state = if (isLoading) {
            EmptyView.State.LOADING
        } else {
            EmptyView.State.EMPTY
        }
    }

    override fun showError(description: String) {
        emptyView.message = description
    }

    override fun hideError() {
        emptyView.setMessage(R.string.empty_set_browse_message)
    }

    private fun setNavigationColor(isLight: Boolean) {
        val color = if (isLight) color(R.color.black87) else Color.WHITE
        val secondaryColor = if (isLight) color(R.color.black54) else color(R.color.white70)
        appbar?.navigationIcon?.setTint(color)
        tabs.setTabTextColors(secondaryColor, color)
        tabs.setSelectedTabIndicatorColor(color)
    }

    companion object {
        private const val EXTRA_EXPANSION = "SetBrowserActivity.Expansion"
        private const val DEFAULT_SET_SIZE = 1000

        fun createIntent(context: Context, expansion: Expansion): Intent {
            val intent = Intent(context, SetBrowserActivity::class.java)
            intent.putExtra(EXTRA_EXPANSION, expansion)
            return intent
        }

        fun createIntent(context: Context, setCode: String): Intent {
            val expansion = Expansion(
                code = setCode,
                totalCards = DEFAULT_SET_SIZE,
                logoUrl = "https://images.pokemontcg.io/$setCode/logo.png"
            )
            return createIntent(context, expansion)
        }
    }
}
