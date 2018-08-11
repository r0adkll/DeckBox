package com.r0adkll.deckbuilder.arch.ui.features.browse


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.graphics.ColorUtils
import android.support.v7.graphics.Palette
import android.support.v7.widget.GridLayoutManager
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi.*
import com.r0adkll.deckbuilder.arch.ui.features.browse.di.SetBrowserModule
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonBuilderRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonItem
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindParcelable
import com.r0adkll.deckbuilder.util.bindString
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.palette.PaletteBitmapViewTarget
import com.r0adkll.deckbuilder.util.palette.PaletteTargetBuilder
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_set_browser.*
import timber.log.Timber
import javax.inject.Inject


class SetBrowserActivity : BaseActivity(), SetBrowserUi, SetBrowserUi.Intentions, SetBrowserUi.Actions {

    private val expansion: Expansion by bindParcelable(EXTRA_EXPANSION)

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: SetBrowserRenderer
    @Inject lateinit var presenter: SetBrowserPresenter

    private val filterChanges: Relay<SetBrowserUi.BrowseFilter> = PublishRelay.create()
    private val cardClicks: Relay<PokemonCardView> = PublishRelay.create()
    private lateinit var adapter: PokemonBuilderRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_browser)

        state = state.copy(setCode = expansion.code, pageSize = expansion.totalCards)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        appbar?.setNavigationOnClickListener { finish() }

        GlideApp.with(this)
                .`as`(PaletteBitmap::class.java)
                .load(expansion.logoUrl)
                .into(PaletteBitmapViewTarget(logo, listOf(TargetPaletteAction())))

        // listen for tab changes
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val filter = when(tab.position) {
                    0 -> BrowseFilter.ALL
                    1 -> BrowseFilter.POKEMON
                    2 -> BrowseFilter.TRAINER
                    3 -> BrowseFilter.ENERGY
                    else -> BrowseFilter.ALL
                }
                filterChanges.accept(filter)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        appBarLayout.addOnOffsetChangedListener { view, offset ->
            val height = view.height.toFloat() - ((appbar?.height ?: 0) + dipToPx(24f))
            val percent = offset.toFloat() / height
            val minScale = 0.4f
            val maxScale = 1f
            val scale = maxScale + ((maxScale - minScale) * percent)
            logo.pivotX = logo.width * 0.5f
            logo.pivotY = logo.height.toFloat() * 0.9f
            logo.scaleX = scale
            logo.scaleY = scale
        }

        disposables += cardClicks
                .subscribe {
                    Analytics.event(Event.SelectContent.PokemonCard(it.card?.id ?: "unknown"))
                    CardDetailActivity.show(this, it)
                }

        adapter = PokemonBuilderRecyclerAdapter(this, EditCardIntentions(), cardClicks)
        adapter.setEmptyView(emptyView)
        recycler.layoutManager = GridLayoutManager(this, 3)
        recycler.adapter = adapter

        renderer.start()
        presenter.start()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(SetBrowserModule(this))
                .inject(this)
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun render(state: SetBrowserUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun filterChanged(): Observable<SetBrowserUi.BrowseFilter> {
        return filterChanges
    }


    override fun setFilter(filter: SetBrowserUi.BrowseFilter) {
        val position = when(filter) {
            BrowseFilter.ALL -> 0
            BrowseFilter.POKEMON -> 1
            BrowseFilter.TRAINER -> 2
            BrowseFilter.ENERGY -> 3
        }
        tabs.getTabAt(position)?.select()
    }


    override fun setCards(cards: List<PokemonCard>) {
        adapter.setPokemon(cards.map { PokemonItem.Single(StackedPokemonCard(it, 1)) })
    }


    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }


    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }


    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_set_browse_message)
    }


    inner class TargetPaletteAction : PaletteBitmapViewTarget.PaletteAction {
        override fun execute(palette: Palette?) {
            palette?.let {
                if (expansion.code != "sm6" && expansion.code != "sm5" && expansion.code != "sm7") {
                    it.vibrantSwatch?.rgb?.let {
                        backdrop.imageTintList = ColorStateList.valueOf(it)

                        // Calculate control color
                        if (ColorUtils.calculateContrast(Color.WHITE, it) < 3.0) {
                            val color = color(R.color.black87)
                            val secondaryColor = color(R.color.black54)
                            appbar?.navigationIcon?.setTint(color)
                            tabs.setTabTextColors(secondaryColor, color)
                            tabs.setSelectedTabIndicatorColor(color)

                        } else {
                            val color = Color.WHITE
                            val secondaryColor = color(R.color.white70)
                            appbar?.navigationIcon?.setTint(color)
                            tabs.setTabTextColors(secondaryColor, color)
                            tabs.setSelectedTabIndicatorColor(color)
                        }


                    }
                    backdrop.imageTintMode = PorterDuff.Mode.ADD
                }
            }
        }
    }

    companion object {
        const val EXTRA_EXPANSION = "SetBrowserActivity.Expansion"

        fun createIntent(context: Context, expansion: Expansion): Intent {
            val intent = Intent(context, SetBrowserActivity::class.java)
            intent.putExtra(EXTRA_EXPANSION, expansion)
            return intent
        }


        fun createIntent(context: Context, setCode: String): Intent {
            val expansion = Expansion(setCode, null, "", "", 300, false, false, "", "", "https://images.pokemontcg.io/$setCode/logo.png")
            return createIntent(context, expansion)
        }
    }
}