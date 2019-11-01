package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.DashPathEffect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.evernote.android.state.State
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.presentation.delegates.StatefulActivityDelegate
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.extensions.sp
import com.ftinc.kit.util.bindLong
import com.ftinc.kit.util.bindOptionalParcelable
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.ui.components.customtab.CustomTabBrowser
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.adapter.PokemonCardsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.carddetail.di.CardDetailModule
import com.r0adkll.deckbuilder.arch.ui.features.marketplace.ProductSparkAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.util.MarketplaceHelper
import com.r0adkll.deckbuilder.util.extensions.drawable
import com.r0adkll.deckbuilder.util.extensions.formatPrice
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_card_detail.*
import kotlinx.android.synthetic.main.layout_card_details.*
import kotlinx.android.synthetic.main.layout_card_information.*
import kotlinx.android.synthetic.main.layout_collection_count_adjuster.*
import kotlinx.android.synthetic.main.layout_marketplace.*
import javax.inject.Inject

class CardDetailActivity : BaseActivity(), CardDetailUi, CardDetailUi.Intentions, CardDetailUi.Actions {

    private val card: PokemonCard? by bindOptionalParcelable(EXTRA_CARD)
    private val sessionId: Long by bindLong(EXTRA_SESSION_ID)

    private val addCardClicks: Relay<Unit> = PublishRelay.create()
    private val removeCardClicks: Relay<Unit> = PublishRelay.create()

    @State override var state: CardDetailUi.State = CardDetailUi.State.DEFAULT

    @Inject lateinit var renderer: CardDetailRenderer
    @Inject lateinit var presenter: CardDetailPresenter

    private lateinit var customTabBrowser: CustomTabBrowser
    private lateinit var variantsAdapter: PokemonCardsRecyclerAdapter
    private lateinit var evolvesFromAdapter: PokemonCardsRecyclerAdapter
    private lateinit var evolvesToAdapter: PokemonCardsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)
        customTabBrowser = CustomTabBrowser(this)

        // Odd state hack to pass in passed values
        state = state.copy(card = card)

        // Only set the deck if it hasn't been set yet
        if (sessionId != -1L) {
            state = state.copy(sessionId = sessionId)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { supportFinishAfterTransition() }

        bindCard()

        // Setup variants recycler
        variantsAdapter = PokemonCardsRecyclerAdapter(this) { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard(view.card?.id ?: "unknown"))
            show(this, view, sessionId)
        }
        variantsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        variantsRecycler.adapter = variantsAdapter

        // Setup evolves from adapter
        evolvesFromAdapter = PokemonCardsRecyclerAdapter(this) { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard(view.card?.id ?: "unknown"))
            show(this, view, sessionId)
        }
        evolvesRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        evolvesRecycler.adapter = evolvesFromAdapter

        // Setup evolves to adapter
        evolvesToAdapter = PokemonCardsRecyclerAdapter(this) { view, _ ->
            Analytics.event(Event.SelectContent.PokemonCard(view.card?.id ?: "unknown"))
            show(this, view, sessionId)
        }
        evolvesToRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        evolvesToRecycler.adapter = evolvesToAdapter

        actionClose?.setOnClickListener { finish() }

        priceSparkline.baseLinePaint.pathEffect = DashPathEffect(floatArrayOf(dp(4f), dp(4f)), 0f)
        priceSparkline.setScrubListener {
            val product = it as? Product
            if (product != null) {
                slidingLayout?.isTouchEnabled = false
                showPrices(product.price?.low, product.price?.market, product.price?.high)
            } else {
                slidingLayout?.isTouchEnabled = true

                // DRAGONS: this is dubious because we are manipulating the view state outside of MVI
                val latestProduct = state.products?.maxBy { it.recordedAt }
                showPrices(latestProduct?.price?.low, latestProduct?.price?.market,
                    latestProduct?.price?.high)
            }
        }

        actionBuy.setOnClickListener {
            val product = state.products?.maxBy { it.recordedAt }
            if (product != null) {
                Analytics.event(Event.ViewItem.MarketplaceLink(product.cardId))
                val url = MarketplaceHelper.buildAffiliateLink(product)
                customTabBrowser.launch(url)
            }
        }

        priceMarketLayout.setOnClickListener {
            Analytics.event(Event.SelectContent.Action("market_price_info"))
            MarketplaceHelper.showMarketPriceExplanationDialog(this)
        }
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

            if (state.sessionId != null) {
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
        return when (item.itemId) {
            R.id.action_add -> {
                Analytics.event(Event.SelectContent.Action("detail_add_card", card?.name))
                addCardClicks.accept(Unit)
                true
            }
            R.id.action_remove -> {
                Analytics.event(Event.SelectContent.Action("detail_remove_card", card?.name))
                removeCardClicks.accept(Unit)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupComponent() {
        DeckApp.component.plus(CardDetailModule(this))
            .inject(this)

        delegates += StatefulActivityDelegate(renderer, Lifecycle.Event.ON_START)
        delegates += StatefulActivityDelegate(presenter, Lifecycle.Event.ON_START)
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

    override fun incrementCollectionCount(): Observable<Unit> {
        return actionAddCollection.clicks()
            .doOnNext {
                Analytics.event(Event.SelectContent.Collection.Increment(card?.id ?: ""))
            }
    }

    override fun decrementCollectionCount(): Observable<Unit> {
        return actionRemoveCollection.clicks()
            .doOnNext {
                Analytics.event(Event.SelectContent.Collection.Decrement(card?.id ?: ""))
            }
    }

    override fun showCopies(count: Int?) {
        supportActionBar?.title = count?.let {
            resources.getQuantityString(R.plurals.card_detail_copies, count, count)
        } ?: " "
        invalidateOptionsMenu()
    }

    override fun showValidation(format: Format) {
        deckFormat.setText(when (format) {
            Format.STANDARD -> R.string.format_standard
            Format.EXPANDED -> R.string.format_expanded
            else -> R.string.format_unlimited
        })
    }

    override fun showCollectionCount(count: Int) {
        actionRemoveCollection.isInvisible = count <= 0
        collectionCount.text = count.toString()
    }

    override fun showPrices(lowPrice: Double?, marketPrice: Double?, highPrice: Double?) {
        costsLayout.isVisible = lowPrice != null || marketPrice != null || highPrice != null
        priceLow.text = lowPrice?.formatPrice() ?: "n/a"
        priceMarket.text = marketPrice?.formatPrice() ?: "n/a"
        priceHigh.text = highPrice?.formatPrice() ?: "n/a"
    }

    override fun showPriceHistory(products: List<Product>) {
        priceSparkline.isVisible = products.isNotEmpty()
        priceSparkline.adapter = ProductSparkAdapter(products)

        // Prepare our product url to load
        val product = products.maxBy { it.recordedAt }
        if (product != null) {
            val url = MarketplaceHelper.buildAffiliateLink(product)
            customTabBrowser.prepare(url)
        }
    }

    override fun showVariants(cards: List<PokemonCard>) {
        variantsAdapter.submitList(cards)
        variantsDivider?.isVisible = cards.isNotEmpty()
        variantsHeader.isVisible = cards.isNotEmpty()
        variantsRecycler.isVisible = cards.isNotEmpty()
    }

    override fun showEvolvesFrom(cards: List<PokemonCard>) {
        evolvesFromAdapter.submitList(cards)
        evolvesDivider?.isVisible = cards.isNotEmpty()
        evolvesHeader.isVisible = cards.isNotEmpty()
        evolvesRecycler.isVisible = cards.isNotEmpty()
    }

    override fun showEvolvesTo(cards: List<PokemonCard>) {
        evolvesToAdapter.submitList(cards)
        evolvesToDivider?.isVisible = cards.isNotEmpty()
        evolvesToHeader.isVisible = cards.isNotEmpty()
        evolvesToRecycler.isVisible = cards.isNotEmpty()
    }

    override fun hideCollectionCounter() {
        collectionCounter.isGone = true
    }

    override fun showCardInformation(card: PokemonCard) {
        cardInformation.isVisible = true

        // Set Ability
        if (card.ability != null) {
            abilityName.text = card.ability.name
            abilityText.text = card.ability.text
        } else {
            abilityLabel.isGone = true
            abilityName.isGone = true
            abilityText.isGone = true
        }

        // Set Attacks
        card.attacks?.forEach { attack ->
            val itemView = layoutInflater.inflate(R.layout.layout_card_attack, attacks, false)
            val attackEnergies = itemView.findViewById<LinearLayout>(R.id.attackEnergies)
            val attackName = itemView.findViewById<TextView>(R.id.attackName)
            val attackDamage = itemView.findViewById<TextView>(R.id.attackDamage)
            val attackText = itemView.findViewById<TextView>(R.id.attackText)
            attack.cost?.forEach { cost ->
                val energy = ImageView(this)
                energy.setImageResource(cost.drawable)
                val lp = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                lp.marginEnd = dip(8f)
                attackEnergies.addView(energy, lp)
            }

            attackName.text = attack.name
            attackDamage.text = attack.damage
            attackText.isVisible = !attack.text.isNullOrBlank()
            attackText.text = attack.text
            attacks.addView(itemView)
        }

        // Set Card Text
        cardText.isVisible = !card.text.isNullOrEmpty()
        cardText.text = card.text?.joinToString("\n")

        // Set Card Weakness
        cardWeaknessLayout.isVisible = !card.weaknesses.isNullOrEmpty()
        card.weaknesses?.first()?.let { effect ->
            cardWeakness.text = effect.value
            cardWeakness.setCompoundDrawablesRelativeWithIntrinsicBounds(effect.type.drawable, 0, 0, 0)
        }

        // Set Card Resistance
        cardResistanceLayout.isVisible = !card.resistances.isNullOrEmpty()
        card.resistances?.first()?.let { effect ->
            cardResistance.text = effect.value
            cardResistance.setCompoundDrawablesRelativeWithIntrinsicBounds(effect.type.drawable, 0, 0, 0)
        }

        // Set Retreat
        cardRetreatLayout.isVisible = !card.retreatCost.isNullOrEmpty()
        card.retreatCost?.let { cost ->
            cost.forEach { type ->
                val energy = ImageView(this)
                energy.setImageResource(type.drawable)
                val lp = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                lp.marginEnd = dip(8f)
                cardRetreat.addView(energy, lp)
            }
        }
    }

    private fun bindCard() {
        card?.let { card ->
            // Set title + Subtitle
            val spannable = SpannableString(card.name)
            val prismIndex = card.name.indexOf("◇")
            if (prismIndex != -1) {
                spannable.setSpan(ImageSpan(
                    this@CardDetailActivity,
                    R.drawable.ic_prism_star
                ), prismIndex, prismIndex + 1, 0)
            }
            cardTitle.text = spannable
            cardSubtitle.text = card.expansion?.name ?: "Unknown Expansion"

            // Set card number
            cardNumber.text = if (ONLY_NUMBER_REGEX.containsMatchIn(card.number)) {
                SpannableString("${card.number} of ${card.expansion?.totalCards}").apply {
                    setSpan(AbsoluteSizeSpan(sp(12f).toInt()), card.number.length, card.number.length + 3, 0)
                    setSpan(ForegroundColorSpan(color(R.color.black54)), card.number.length, card.number.length + 3, 0)
                }
            } else {
                card.number
            }

            // Load card image
            emptyView.isVisible = true
            emptyView.state = EmptyView.State.LOADING
            var request = GlideApp.with(this)
                .load(card.imageUrlHiRes)
                .transition(withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        emptyView.setMessage(R.string.image_loading_error)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        emptyView.isGone = true
                        return false
                    }
                })

            if (slidingLayout == null) {
                request = request.placeholder(R.drawable.pokemon_card_back)
            }

            @Suppress("PLUGIN_WARNING")
            request.into(image ?: tabletImage)

            // Load expansion symbol
            GlideApp.with(this)
                .load(card.expansion?.symbolUrl)
                .transition(withCrossFade())
                .into(expansionSymbol)
        }
    }

    companion object {
        const val EXTRA_CARD = "CardDetailActivity.Card"
        const val EXTRA_SESSION_ID = "CardDetailActivity.SessionId"
        private val ONLY_NUMBER_REGEX by lazy { "^[0-9]+".toRegex() }

        fun createIntent(
            context: Context,
            card: PokemonCard,
            sessionId: Long? = null
        ): Intent {
            val intent = Intent(context, CardDetailActivity::class.java)
            intent.putExtra(EXTRA_CARD, card)
            sessionId?.let { if (it != Session.NO_ID) intent.putExtra(EXTRA_SESSION_ID, it) }
            return intent
        }

        fun show(
            context: Activity,
            view: PokemonCardView,
            sessionId: Long? = null
        ) {
            if (view.card != null) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "cardImage")
                val intent = createIntent(context, view.card!!, sessionId)
                context.startActivity(intent, options.toBundle())
            }
        }
    }
}
