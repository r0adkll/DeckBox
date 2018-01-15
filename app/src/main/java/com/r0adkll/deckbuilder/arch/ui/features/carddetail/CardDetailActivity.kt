package com.r0adkll.deckbuilder.arch.ui.features.carddetail


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.SharedElementCallback
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.evernote.android.state.State
import com.ftinc.kit.kotlin.extensions.*
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Validation
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.adapter.PokemonCardsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailModule
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindBoolean
import com.r0adkll.deckbuilder.util.bindParcelable
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_card_detail.*
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject


class CardDetailActivity : BaseActivity(), CardDetailUi, CardDetailUi.Actions {

    private val card: PokemonCard by bindParcelable(EXTRA_CARD)
    private val editMode: Boolean by bindBoolean(EXTRA_EDIT_MODE)


    @State override var state: CardDetailUi.State = CardDetailUi.State(null, emptyList(), emptyList(), Validation(false, false))

    @Inject lateinit var renderer: CardDetailRenderer
    @Inject lateinit var presenter: CardDetailPresenter

    private lateinit var variantsAdapter: PokemonCardsRecyclerAdapter
    private lateinit var evolvesAdapter: PokemonCardsRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        // Odd state hack to pass in passed values
        state = state.copy(card = card)

        bindCard()

        variantsAdapter = PokemonCardsRecyclerAdapter(this)
        variantsAdapter.setOnViewItemClickListener { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard((view as PokemonCardView).card?.id ?: "unknown"))
            CardDetailActivity.show(this, view, editMode)
        }
        variantsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        variantsRecycler.adapter = variantsAdapter

        evolvesAdapter = PokemonCardsRecyclerAdapter(this)
        evolvesAdapter.setOnViewItemClickListener { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard((view as PokemonCardView).card?.id ?: "unknown"))
            CardDetailActivity.show(this, view, editMode)
        }
        evolvesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        evolvesRecycler.adapter = evolvesAdapter

        renderer.start()
        presenter.start()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val editModeResult = parseResult(resultCode, requestCode, data)
        if (editModeResult != null) {
            setEditResult(editModeResult)
            supportFinishAfterTransition()
        }
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if (slidingLayout != null) {
            menuInflater.inflate(R.menu.activity_card_detail, menu)
            true
        } else {
            false
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_add -> {
                setEditResult(card)
                supportFinishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(CardDetailModule(this))
                .inject(this)
    }


    override fun render(state: CardDetailUi.State) {
        this.state = state
        renderer.render(state)
    }


    override fun showStandardValidation(isValid: Boolean) {
        formatStandard.setVisible(isValid)
    }


    override fun showExpandedValidation(isValid: Boolean) {
        formatExpanded.setVisible(isValid)
    }


    override fun showVariants(cards: List<PokemonCard>) {
        variantsAdapter.setCards(cards)
        variantsHeader.setVisible(cards.isNotEmpty())
        variantsRecycler.setVisible(cards.isNotEmpty())
    }


    override fun showEvolvesFrom(cards: List<PokemonCard>) {
        evolvesAdapter.setCards(cards)
        evolvesHeader.setVisible(cards.isNotEmpty())
        evolvesRecycler.setVisible(cards.isNotEmpty())
    }


    private fun setEditResult(card: PokemonCard) {
        val data = Intent()
        data.putExtra(EXTRA_CARD, card)
        setResult(RESULT_OK, data)
    }


    private fun bindCard() {
        val number = "#${card.number}"
        val name = " ${card.name}"
        val spannable = SpannableString("$number$name")
        val color = if (slidingLayout == null) color(R.color.black56) else color(R.color.white70)
        spannable.setSpan(ForegroundColorSpan(color), 0, number.length, 0)
        cardTitle.text = spannable
        cardSubtitle.text = card.expansion?.name ?: "Unknown Expansion"

        emptyView.visible()
        emptyView.setLoading(true)
        var request = GlideApp.with(this)
                .load(card.imageUrlHiRes)
                .transition(withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        emptyView.setEmptyMessage(R.string.image_loading_error)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        emptyView.gone()
                        return false
                    }
                })

        if (slidingLayout == null) {
            request = request.placeholder(R.drawable.pokemon_card_back)
        }

        request.into(image ?: tabletImage)


        GlideApp.with(this)
                .load(card.expansion?.symbolUrl)
                .transition(withCrossFade())
                .into(expansionSymbol)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        actionClose?.setOnClickListener { finish() }


        slidingLayout?.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                val rotation = 180f * slideOffset
                panelArrow?.rotation = rotation
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
            }
        })
    }


    companion object {
        @JvmField val EXTRA_CARD = "CardDetailActivity.Card"
        @JvmField val EXTRA_EDIT_MODE = "CardDetailActivity.EditMode"
        @JvmField val RC_EDIT_CARD = 1


        fun createIntent(context: Context, card: PokemonCard, editMode: Boolean = false): Intent {
            val intent = Intent(context, CardDetailActivity::class.java)
            intent.putExtra(EXTRA_CARD, card)
            intent.putExtra(EXTRA_EDIT_MODE, editMode)
            return intent
        }


        fun show(context: Activity, view: PokemonCardView, editMode: Boolean = false) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "cardImage")
            val intent = CardDetailActivity.createIntent(context, view.card!!, editMode)
            if (editMode) {
                context.startActivityForResult(intent, RC_EDIT_CARD, options.toBundle())
            } else {
                context.startActivity(intent, options.toBundle())
            }
        }


        fun parseResult(resultCode: Int, requestCode: Int, data: Intent?): PokemonCard? {
            return if (resultCode == RESULT_OK && requestCode == RC_EDIT_CARD) {
                data?.getParcelableExtra(EXTRA_CARD)
            } else {
                null
            }
        }
    }
}