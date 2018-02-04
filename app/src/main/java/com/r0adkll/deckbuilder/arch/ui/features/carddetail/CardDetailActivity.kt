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
import android.text.style.ImageSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.evernote.android.state.State
import com.ftinc.kit.kotlin.extensions.*
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.adapter.PokemonCardsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailModule
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.bindBoolean
import com.r0adkll.deckbuilder.util.bindOptionalParcelableList
import com.r0adkll.deckbuilder.util.bindParcelable
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_card_detail.*
import javax.inject.Inject


class CardDetailActivity : BaseActivity(), CardDetailUi, CardDetailUi.Intentions, CardDetailUi.Actions {

    private val card: PokemonCard by bindParcelable(EXTRA_CARD)
    private val deck: List<PokemonCard>? by bindOptionalParcelableList(EXTRA_CARDS)
    private val useLowRes: Boolean by bindBoolean(EXTRA_LOW_RES, false)

    private val addCardClicks: Relay<Unit> = PublishRelay.create()
    private val removeCardClicks: Relay<Unit> = PublishRelay.create()
    private val deckUpdated: Relay<List<PokemonCard>> = PublishRelay.create()

    @State override var state: CardDetailUi.State = CardDetailUi.State.DEFAULT

    @Inject lateinit var renderer: CardDetailRenderer
    @Inject lateinit var presenter: CardDetailPresenter

    private lateinit var variantsAdapter: PokemonCardsRecyclerAdapter
    private lateinit var evolvesAdapter: PokemonCardsRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        // Odd state hack to pass in passed values
        state = state.copy(card = card)

        // Only set the deck if it hasn't been set yet
        if (state.deck == null) {
            state = state.copy(deck = deck)
        }

        bindCard()

        variantsAdapter = PokemonCardsRecyclerAdapter(this)
        variantsAdapter.setOnViewItemClickListener { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard((view as PokemonCardView).card?.id ?: "unknown"))
            CardDetailActivity.show(this, view, state.deck)
        }
        variantsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        variantsRecycler.adapter = variantsAdapter

        evolvesAdapter = PokemonCardsRecyclerAdapter(this)
        evolvesAdapter.setOnViewItemClickListener { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard((view as PokemonCardView).card?.id ?: "unknown"))
            CardDetailActivity.show(this, view, state.deck)
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
            deckUpdated.accept(editModeResult)
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


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (slidingLayout != null) {
            val actionAdd = menu.findItem(R.id.action_add)
            val actionRemove = menu.findItem(R.id.action_remove)

            if (state.deck != null) {
                actionAdd.isVisible = true
                actionRemove.isVisible = state.hasCopies
            } else {
                actionAdd.isVisible = false
                actionRemove.isVisible = false
            }

            return true
        }
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_add -> {
                Analytics.event(Event.SelectContent.Action("detail_add_card", card.name))
                addCardClicks.accept(Unit)
                true
            }
            R.id.action_remove -> {
                Analytics.event(Event.SelectContent.Action("detail_remove_card", card.name))
                removeCardClicks.accept(Unit)
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


    override fun addCardClicks(): Observable<Unit> {
        return addCardClicks
    }


    override fun removeCardClicks(): Observable<Unit> {
        return removeCardClicks
    }


    override fun updateDeck(): Observable<List<PokemonCard>> {
        return deckUpdated
    }


    override fun showCopies(count: Int?) {
        supportActionBar?.title = count?.let {
            resources.getQuantityString(R.plurals.card_detail_copies, count, count)
        } ?: " "
        invalidateOptionsMenu()
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


    override fun setEditResults(cards: List<PokemonCard>?) {
        if (cards != null) {
            val data = Intent()
            data.putParcelableArrayListExtra(EXTRA_CARDS, ArrayList(cards))
            setResult(RESULT_OK, data)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }
    }


    private fun bindCard() {
        val number = "#${card.number}"
        val name = " ${card.name}"
        val spannable = SpannableString("$number$name")
        val color = if (slidingLayout == null) color(R.color.black56) else color(R.color.white70)
        spannable.setSpan(ForegroundColorSpan(color), 0, number.length, 0)

        val prismIndex = name.indexOf("◇")
        if (prismIndex != -1) {
            val startIndex = number.length + prismIndex
            spannable.setSpan(ImageSpan(this@CardDetailActivity, R.drawable.ic_prism_star), startIndex, startIndex + 1, 0)
        }

        cardTitle.text = spannable
        cardSubtitle.text = card.expansion?.name ?: "Unknown Expansion"

        emptyView.visible()
        emptyView.setLoading(true)
        var request = GlideApp.with(this)
                .load(if (useLowRes) card.imageUrl else card.imageUrlHiRes)
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
        @JvmField val EXTRA_CARDS = "CardDetailActivity.Cards"
        @JvmField val EXTRA_LOW_RES = "CardDetailActivity.LowRes"
        @JvmField val RC_EDIT_CARD = 1


        fun createIntent(context: Context, card: PokemonCard, cards: List<PokemonCard>? = null): Intent {
            val intent = Intent(context, CardDetailActivity::class.java)
            intent.putExtra(EXTRA_CARD, card)
            cards?.let { intent.putParcelableArrayListExtra(EXTRA_CARDS, ArrayList(cards)) }
            return intent
        }


        fun show(context: Activity, view: PokemonCardView, cards: List<PokemonCard>? = null, useLowRes: Boolean = false) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "cardImage")
            val intent = CardDetailActivity.createIntent(context, view.card!!, cards)
            intent.putExtra(EXTRA_LOW_RES, useLowRes)
            if (cards != null) {
                context.startActivityForResult(intent, RC_EDIT_CARD, options.toBundle())
            } else {
                context.startActivity(intent, options.toBundle())
            }
        }


        fun parseResult(resultCode: Int, requestCode: Int, data: Intent?): List<PokemonCard>? {
            return if (resultCode == RESULT_OK && requestCode == RC_EDIT_CARD) {
                data?.getParcelableArrayListExtra(EXTRA_CARDS)
            } else {
                null
            }
        }
    }
}