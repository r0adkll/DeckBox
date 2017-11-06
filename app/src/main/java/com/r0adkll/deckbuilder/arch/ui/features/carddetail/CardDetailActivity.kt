package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.evernote.android.state.State
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.setVisible
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.adapter.PokemonCardsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailModule
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindParcelable
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_card_detail.*
import javax.inject.Inject


class CardDetailActivity : BaseActivity(), CardDetailUi, CardDetailUi.Actions {

    private val card: PokemonCard by bindParcelable(EXTRA_CARD)

    @State override var state: CardDetailUi.State = CardDetailUi.State(null, emptyList(), emptyList())

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
        variantsAdapter.setOnViewItemClickListener { view, card ->
            CardDetailActivity.show(this, view as PokemonCardView)
        }
        variantsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        variantsRecycler.adapter = variantsAdapter

        evolvesAdapter = PokemonCardsRecyclerAdapter(this)
        evolvesAdapter.setOnViewItemClickListener { view, card ->
            CardDetailActivity.show(this, view as PokemonCardView)
        }
        evolvesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        evolvesRecycler.adapter = evolvesAdapter

        renderer.start()
        presenter.start()
    }


    override fun onDestroy() {
        presenter.stop()
        renderer.stop()
        super.onDestroy()
    }


    override fun setupComponent(component: AppComponent) {
        component.plus(CardDetailModule(this))
                .inject(this)
    }


    override fun render(state: CardDetailUi.State) {
        this.state = state
        renderer.render(state)
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


    private fun bindCard() {
        val number = "#${card.number}"
        val name = " ${card.name}"
        val spannable = SpannableString("$number$name")
        spannable.setSpan(ForegroundColorSpan(color(R.color.white70)), 0, number.length, 0)
        cardTitle.text = spannable
        cardSubtitle.text = card.expansion?.name ?: "Unknown Expansion"
        formatStandard.setVisible(card.expansion?.standardLegal ?: false)
        formatExpanded.setVisible(card.expansion?.expandedLegal ?: false)

        GlideApp.with(this)
                .load(card.imageUrlHiRes)
                .transition(withCrossFade())
                .into(image)

        GlideApp.with(this)
                .load(card.expansion?.symbolUrl)
                .transition(withCrossFade())
                .into(expansionSymbol)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }


        slidingLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                val rotation = 180f * slideOffset
                panelArrow.rotation = rotation
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
            }
        })
    }


    companion object {
        @JvmField val EXTRA_CARD = "CardDetailActivity.Card"

        fun createIntent(context: Context, card: PokemonCard): Intent {
            val intent = Intent(context, CardDetailActivity::class.java)
            intent.putExtra(EXTRA_CARD, card)
            return intent
        }


        fun show(context: Activity, view: PokemonCardView) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "cardImage")
            context.startActivity(CardDetailActivity.createIntent(context, view.card!!), options.toBundle())
        }
    }
}