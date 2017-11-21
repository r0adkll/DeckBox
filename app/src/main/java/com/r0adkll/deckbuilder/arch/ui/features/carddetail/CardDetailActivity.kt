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
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindParcelable
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_card_detail.*
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject


class CardDetailActivity : BaseActivity(), CardDetailUi, CardDetailUi.Actions {

    private val card: PokemonCard by bindParcelable(EXTRA_CARD)

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
            CardDetailActivity.show(this, view as PokemonCardView)
        }
        variantsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        variantsRecycler.adapter = variantsAdapter

        evolvesAdapter = PokemonCardsRecyclerAdapter(this)
        evolvesAdapter.setOnViewItemClickListener { view, _ ->
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


    private fun bindCard() {
        val number = "#${card.number}"
        val name = " ${card.name}"
        val spannable = SpannableString("$number$name")
        spannable.setSpan(ForegroundColorSpan(color(R.color.white70)), 0, number.length, 0)
        cardTitle.text = spannable
        cardSubtitle.text = card.expansion?.name ?: "Unknown Expansion"

//        supportPostponeEnterTransition()
        emptyView.visible()
        emptyView.setLoading(true)
        GlideApp.with(this)
                .load(card.imageUrlHiRes)
                .transition(withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                        supportStartPostponedEnterTransition()
                        emptyView.setEmptyMessage(R.string.image_loading_error)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                        supportStartPostponedEnterTransition()
                        emptyView.gone()
                        return false
                    }
                })
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