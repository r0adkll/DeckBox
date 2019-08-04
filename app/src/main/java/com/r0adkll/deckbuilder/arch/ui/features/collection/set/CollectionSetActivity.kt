package com.r0adkll.deckbuilder.arch.ui.features.collection.set


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
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.util.UIUtils
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.CardDetailActivity
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.adapter.CollectionSetRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.di.CollectionSetModule
import com.r0adkll.deckbuilder.util.ScreenUtils
import com.r0adkll.deckbuilder.util.ScreenUtils.smallestWidth
import com.r0adkll.deckbuilder.util.bindParcelable
import com.r0adkll.deckbuilder.util.extensions.layoutHeight
import com.r0adkll.deckbuilder.util.extensions.margins
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapViewTarget
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_collection_set.*
import javax.inject.Inject
import kotlin.math.roundToInt


class CollectionSetActivity : BaseActivity(), CollectionSetUi, CollectionSetUi.Intentions, CollectionSetUi.Actions {

    private val expansion: Expansion by bindParcelable(EXTRA_EXPANSION)

    override var state: State = State.DEFAULT

    @Inject lateinit var presenter: CollectionSetPresenter
    @Inject lateinit var renderer: CollectionSetRenderer

    private val addCardClicks = PublishRelay.create<List<PokemonCard>>()
    private val removeCardClicks = PublishRelay.create<PokemonCard>()

    private lateinit var adapter: CollectionSetRecyclerAdapter
    private var statusBarHeight: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_set)

        GlideApp.with(this)
                .`as`(PaletteBitmap::class.java)
                .load(expansion.logoUrl)
                .into(PaletteBitmapViewTarget(logo, listOf(TargetPaletteAction())))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = " "
        appbar?.setNavigationOnClickListener { finish() }

        adapter = CollectionSetRecyclerAdapter(this, removeCardClicks, addCardClicks)
        adapter.setEmptyView(emptyView)
        adapter.setOnItemClickListener {
            addCardClicks.accept(listOf(it.card))
        }

        adapter.setOnItemLongClickListener { _, stackedPokemonCard ->
            startActivity(CardDetailActivity.createIntent(this, stackedPokemonCard.card))
            true
        }

        val spanCount = if (smallestWidth(ScreenUtils.Config.TABLET_10)) 9 else 3
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, spanCount)

        statusBarHeight = UIUtils.getStatusBarHeight(this)
//        appBarLayout.addLayoutHeight((statusBarHeight - dipToPx(24f)))
        appbar?.margins(top = statusBarHeight)
        logo?.layoutHeight(dipToPx(100f))
        logo?.margins(top = statusBarHeight + dipToPx(16f))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_collection_set, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_filter -> {
                // TODO: Show filter
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
    }

    override fun removeCard(): Observable<PokemonCard> {
        return removeCardClicks
    }

    override fun showOverallProgress(progress: Float) {
        progressBar.progress = progress
        progressCompletion.text = getString(R.string.completion_format, progress.times(100f).roundToInt().coerceIn(0, 100))
    }

    override fun showCollection(cards: List<StackedPokemonCard>) {
        adapter.setCollectionItems(cards)
    }

    override fun hideError() {
        emptyView.setEmptyMessage(R.string.empty_collection_set)
    }

    override fun showError(description: String) {
        emptyView.emptyMessage = description
    }

    override fun showLoading(isLoading: Boolean) {
        emptyView.setLoading(isLoading)
    }

    inner class TargetPaletteAction : PaletteBitmapViewTarget.PaletteAction {
        override fun execute(palette: androidx.palette.graphics.Palette?) {
            palette?.let { p ->
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
                        "sm5" -> {
                            setNavigationColor(Color.BLACK)
                        }
                        else -> {
                            backdrop.imageTintList = ColorStateList.valueOf(it)
                            backdrop.imageTintMode = PorterDuff.Mode.ADD
                            setNavigationColor(it)
                        }
                    }
                } ?: setNavigationColor(Color.BLACK)
            }
        }

        private fun setNavigationColor(@ColorInt navigationColor: Int) {
            // Calculate control color
            if (ColorUtils.calculateContrast(Color.WHITE, navigationColor) < 3.0) {
                val color = color(R.color.black87)
                val secondaryColor = color(R.color.black54)
                appbar?.navigationIcon?.setTint(color)
                progressCompletion.setTextColor(color)
                progressBar.borderColor = color
                progressBar.trackColor = secondaryColor
                val size = appbar?.menu?.size() ?: 0
                (0 until size).forEach {
                    appbar?.menu?.getItem(it)?.icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
            } else {
                val color = Color.WHITE
                val secondaryColor = color(R.color.white50)
                appbar?.navigationIcon?.setTint(color)
                progressCompletion.setTextColor(color)
                progressBar.borderColor = color
                progressBar.trackColor = secondaryColor
                val size = appbar?.menu?.size() ?: 0
                (0 until size).forEach {
                    appbar?.menu?.getItem(it)?.icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

    companion object {
        private const val EXTRA_EXPANSION = "CollectionSetActivity.Expansion"

        fun createIntent(context: Context, expansion: Expansion): Intent {
            val intent = Intent(context, CollectionSetActivity::class.java)
            intent.putExtra(EXTRA_EXPANSION, expansion)
            return intent
        }
    }
}