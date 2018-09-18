package com.r0adkll.deckbuilder.arch.ui.widgets


import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.util.Pools
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.CardUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class MarqueeCardContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var cardWidth: Int = dipToPx(CARD_WIDTH)
    private val cardSpacing: Int = dipToPx(CARD_SPACING)

    private val cardPool: Pools.Pool<PokemonCardView> = Pools.SimplePool(NUMBER_CARDS)
    private val cards: MutableList<PokemonCardView> = ArrayList(NUMBER_CARDS)

    private var disposable: Disposable? = null
    private var imageIndex = 0

    init {
        // Initialize at least 5 views
        (0..NUMBER_CARDS).forEach { _ ->
            addNewCard()
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeCardContainer, defStyleAttr, 0)
        a?.let {
            cardWidth = it.getDimensionPixelSize(R.styleable.MarqueeCardContainer_cardWidth, dipToPx(CARD_WIDTH))
            a.recycle()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val cardHeight = measuredHeight - (2 * dipToPx(64f))
        cardWidth = (cardHeight / PokemonCardView.RATIO).toInt()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupAnimator()
    }

    override fun onDetachedFromWindow() {
        disposable?.dispose()
        super.onDetachedFromWindow()
    }


    @SuppressLint("RxSubscribeOnError", "RxDefaultScheduler")
    private fun setupAnimator() {
        disposable = Observable.interval(25L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    cards.forEachIndexed { _, card ->
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
        if (imageIndex >= SHUFFLED_CARDS.size) {
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
            val cards = mutableListOf(*CardUtils.CARDS)
            cards.shuffle()
            cards
        }
    }
}