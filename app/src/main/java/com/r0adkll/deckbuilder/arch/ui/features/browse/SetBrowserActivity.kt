package com.r0adkll.deckbuilder.arch.ui.features.browse


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.util.UIUtils
import com.google.android.material.appbar.AppBarLayout
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi.BrowseFilter
import com.r0adkll.deckbuilder.arch.ui.features.browse.SetBrowserUi.State
import com.r0adkll.deckbuilder.arch.ui.features.browse.di.SetBrowserModule
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonBuilderRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonItem
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.ScreenUtils.Config.TABLET_10
import com.r0adkll.deckbuilder.util.ScreenUtils.smallestWidth
import com.r0adkll.deckbuilder.util.bindParcelable
import com.r0adkll.deckbuilder.util.extensions.addLayoutHeight
import com.r0adkll.deckbuilder.util.extensions.layoutHeight
import com.r0adkll.deckbuilder.util.extensions.margins
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapViewTarget
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_set_browser.*
import javax.inject.Inject


class SetBrowserActivity : BaseActivity(), SetBrowserUi, SetBrowserUi.Intentions, SetBrowserUi.Actions {

    private val expansion: Expansion by bindParcelable(EXTRA_EXPANSION)

    override var state: State = State.DEFAULT

    @Inject lateinit var renderer: SetBrowserRenderer
    @Inject lateinit var presenter: SetBrowserPresenter

    private val filterChanges: Relay<SetBrowserUi.BrowseFilter> = PublishRelay.create()
    private val cardClicks: Relay<PokemonCardView> = PublishRelay.create()
    private lateinit var adapter: PokemonBuilderRecyclerAdapter
    
    private var statusBarHeight: Int = 0
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_browser)

        state = state.copy(setCode = expansion.code, pageSize = expansion.totalCards + 100 /* Hack to account for secret rares */)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        appbar?.setNavigationOnClickListener { finish() }

        GlideApp.with(this)
                .`as`(PaletteBitmap::class.java)
                .load(expansion.logoUrl)
                .into(PaletteBitmapViewTarget(logo, listOf(TargetPaletteAction())))

        // listen for tab changes
        // FIXME: Hack
        BrowseFilter.values().forEachIndexed { index, browseFilter ->
            tabs.getTabAt(index)?.tag = browseFilter.name
        }

        tabs.tabMode = if (smallestWidth(TABLET_10)) com.google.android.material.tabs.TabLayout.MODE_FIXED else com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
        tabs.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab) {
                val filter = when(tab.tag as? String) {
                    "ALL" -> BrowseFilter.ALL
                    "POKEMON" -> BrowseFilter.POKEMON
                    "TRAINER" -> BrowseFilter.TRAINER
                    "ENERGY" -> BrowseFilter.ENERGY
                    "GX" -> BrowseFilter.GX
                    "PRISM" -> BrowseFilter.PRISM
                    else -> BrowseFilter.ALL
                }
                filterChanges.accept(filter)
                Analytics.event(Event.SelectContent.Action("expansion_filter", filter.name))
            }

            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

        statusBarHeight = UIUtils.getStatusBarHeight(this)
        appBarLayout.addLayoutHeight((statusBarHeight - dipToPx(24f)))
        appbar?.margins(top = statusBarHeight)
        logo?.layoutHeight(dipToPx(100f))
        logo?.margins(top = (statusBarHeight - dipToPx(24f)) / 2)

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

        val spanCount = if (smallestWidth(TABLET_10)) 9 else 3
        adapter = PokemonBuilderRecyclerAdapter(this, spanCount, EditCardIntentions(), cardClicks)
        adapter.setEmptyView(emptyView)
        recycler.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, spanCount)
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
        for (i in (0 until tabs.tabCount)) {
            val tab = tabs.getTabAt(i)
            val tag = tab?.tag as? String
            if (tag?.equals(filter.name, true) == true) {
                tab.select()
                break
            }
        }
    }


    override fun hideFilters(vararg filters: BrowseFilter) {
        filters.forEach { filter ->
            for (i in (0 until tabs.tabCount)) {
                val tag = tabs.getTabAt(i)?.tag as? String
                if (tag?.equals(filter.name, true) == true) {
                    tabs.removeTabAt(i)
                    break
                }
            }
        }

        if (!smallestWidth(TABLET_10) && tabs.tabCount <= 4) {
            tabs.tabMode = com.google.android.material.tabs.TabLayout.MODE_FIXED
        }
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
        override fun execute(palette: androidx.palette.graphics.Palette?) {
            palette?.let { p ->
                if (expansion.code != "sm5") {
                    p.vibrantSwatch?.rgb?.let {
                        when(expansion.code) {
                            "sm75" -> {
                                val background = ColorDrawable(it)
                                val pattern = BitmapFactory.decodeResource(resources, R.drawable.dr_scales_pattern)
                                val foreground = BitmapDrawable(resources, pattern).apply {
                                    setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                                    setTargetDensity(resources.displayMetrics.densityDpi * 4)
                                }
                                backdrop.setImageDrawable(LayerDrawable(arrayOf(background, foreground)))
                                setNavigationColor(it)
                            }
                            "sm8" -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    backdrop.setImageResource(R.drawable.dr_smlt_header)
                                    setNavigationColor(Color.BLACK)
                                } else {
                                    backdrop.imageTintList = ColorStateList.valueOf(it)
                                    backdrop.imageTintMode = PorterDuff.Mode.ADD
                                    setNavigationColor(it)
                                }
                            }
                            "sm9" -> {
                                backdrop.setImageResource(R.drawable.dr_smtu_background)
                                setNavigationColor(Color.BLACK)
                            }
                            else -> {
                                backdrop.imageTintList = ColorStateList.valueOf(it)
                                backdrop.imageTintMode = PorterDuff.Mode.ADD
                                setNavigationColor(it)
                            }
                        }
                    }
                }
            }
        }

        private fun setNavigationColor(@ColorInt navigationColor: Int) {
            // Calculate control color
            if (ColorUtils.calculateContrast(Color.WHITE, navigationColor) < 3.0) {
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