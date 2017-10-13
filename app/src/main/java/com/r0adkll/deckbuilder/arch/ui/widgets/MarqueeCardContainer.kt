package com.r0adkll.deckbuilder.arch.ui.widgets


import android.content.Context
import android.support.v4.util.Pools
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.util.Utils
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MarqueeCardContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val cardWidth: Int = dipToPx(CARD_WIDTH)
    private val cardSpacing: Int = dipToPx(CARD_SPACING)

    private val cardPool: Pools.Pool<PokemonCardView> = Pools.SimplePool(NUMBER_CARDS)
    private val cards: MutableList<PokemonCardView> = ArrayList(NUMBER_CARDS)

    private var disposable: Disposable? = null
    private var imageIndex = 0

    init {
        // Initialize at least 5 views
        (0..NUMBER_CARDS).forEach {
            addNewCard()
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupAnimator()
    }

    override fun onDetachedFromWindow() {
        disposable?.dispose()
        super.onDetachedFromWindow()
    }


    private fun setupAnimator() {
        disposable = Observable.interval(25L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    cards.forEachIndexed { index, card ->
                        card.translationX -= dipToPx(0.75f)
                    }

                    for ((index, value) in cards.withIndex()) {
                        if (value.translationX < -cardWidth) {
                            val card = cards.removeAt(index)
                            removeView(card)
                            cardPool.release(card)
                            addNewCard()
                            break
                        }
                    }
                }
    }


    private fun addNewCard() {
        var view = cardPool.acquire()
        if (view == null) {
            view = PokemonCardView(context)
        }

        val lastItemX = cards.lastOrNull()?.let {it.translationX + cardSpacing + cardWidth} ?: 0f
        view.translationX = lastItemX
        cards += view

        val lp = LayoutParams(cardWidth, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_VERTICAL
        addView(view, lp)

        val imageUrl = SHUFFLED_CARDS[imageIndex]
        ++imageIndex
        if (imageIndex >= CARDS.size) {
            imageIndex = 0
        }
        GlideApp.with(this)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.pokemon_card_back)
                .into(view)
    }

    companion object {
        const val NUMBER_CARDS = 6
        const val CARD_WIDTH = 120f
        const val CARD_SPACING = 16f

        val SHUFFLED_CARDS by lazy {
            val cards = mutableListOf(*CARDS)
            Collections.shuffle(cards)
            cards
        }
        val CARDS: Array<String> = arrayOf(
                "https://images.pokemontcg.io/base1/4_hires.png",
                "https://images.pokemontcg.io/base6/18_hires.png",
                "https://images.pokemontcg.io/base1/2_hires.png",
                "https://images.pokemontcg.io/base1/10_hires.png",
                "https://images.pokemontcg.io/sm3/137_hires.png",
                "https://images.pokemontcg.io/sm3/131_hires.png",
                "https://images.pokemontcg.io/sm2/20_hires.png",
                "https://images.pokemontcg.io/sm2/40_hires.png",
                "https://images.pokemontcg.io/sm2/41_hires.png",
                "https://images.pokemontcg.io/sm2/41_hires.png"
        )
    }
}