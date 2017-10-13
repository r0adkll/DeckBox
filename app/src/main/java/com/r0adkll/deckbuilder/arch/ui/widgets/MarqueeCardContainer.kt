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
                "https://images.pokemontcg.io/sm1/107.png",
                "https://images.pokemontcg.io/sm2/121.png",
                "https://images.pokemontcg.io/sm2/40.png",
                "https://images.pokemontcg.io/sm2/44.png",
                "https://images.pokemontcg.io/sm2/103.png",
                "https://images.pokemontcg.io/sm2/59.png",
                "https://images.pokemontcg.io/sm2/98.png",
                "https://images.pokemontcg.io/sm2/62.png",
                "https://images.pokemontcg.io/sm3/56.png",
                "https://images.pokemontcg.io/sm3/29.png",
                "https://images.pokemontcg.io/sm1/119.png",
                "https://images.pokemontcg.io/sm1/84.png",
                "https://images.pokemontcg.io/sm1/122.png",
                "https://images.pokemontcg.io/sm3/90.png",
                "https://images.pokemontcg.io/sm2/57.png",
                "https://images.pokemontcg.io/sm2/63.png",
                "https://images.pokemontcg.io/sm1/103.png",
                "https://images.pokemontcg.io/sm2/13.png",
                "https://images.pokemontcg.io/sm3/22.png",
                "https://images.pokemontcg.io/sm1/100.png",
                "https://images.pokemontcg.io/sm1/106.png",
                "https://images.pokemontcg.io/sm3/38.png",
                "https://images.pokemontcg.io/sm3/5.png",
                "https://images.pokemontcg.io/sm3/42.png",
                "https://images.pokemontcg.io/sm1/132.png",
                "https://images.pokemontcg.io/sm2/56.png",
                "https://images.pokemontcg.io/sm2/67.png",
                "https://images.pokemontcg.io/sm3/124.png",
                "https://images.pokemontcg.io/sm1/93.png",
                "https://images.pokemontcg.io/sm1/104.png",
                "https://images.pokemontcg.io/sm3/48.png",
                "https://images.pokemontcg.io/sm1/35.png",
                "https://images.pokemontcg.io/sm2/43.png",
                "https://images.pokemontcg.io/sm1/137.png",
                "https://images.pokemontcg.io/sm3/41.png",
                "https://images.pokemontcg.io/sm2/60.png",
                "https://images.pokemontcg.io/sm1/24.png",
                "https://images.pokemontcg.io/sm1/62.png",
                "https://images.pokemontcg.io/sm2/131.png",
                "https://images.pokemontcg.io/sm2/107.png",
                "https://images.pokemontcg.io/sm2/29.png",
                "https://images.pokemontcg.io/sm1/101.png",
                "https://images.pokemontcg.io/sm1/69.png",
                "https://images.pokemontcg.io/sm2/99.png",
                "https://images.pokemontcg.io/sm3/21.png",
                "https://images.pokemontcg.io/sm2/141.png",
                "https://images.pokemontcg.io/sm1/128.png",
                "https://images.pokemontcg.io/sm3/117.png",
                "https://images.pokemontcg.io/sm1/121.png",
                "https://images.pokemontcg.io/sm1/46.png",
                "https://images.pokemontcg.io/sm1/5.png",
                "https://images.pokemontcg.io/sm1/111.png",
                "https://images.pokemontcg.io/sm2/39.png",
                "https://images.pokemontcg.io/sm3/95.png",
                "https://images.pokemontcg.io/sm3/77.png",
                "https://images.pokemontcg.io/sm3/98.png",
                "https://images.pokemontcg.io/sm1/133.png",
                "https://images.pokemontcg.io/sm2/36.png",
                "https://images.pokemontcg.io/sm1/143.png",
                "https://images.pokemontcg.io/sm3/71.png",
                "https://images.pokemontcg.io/sm3/51.png",
                "https://images.pokemontcg.io/sm2/45.png",
                "https://images.pokemontcg.io/sm2/32.png",
                "https://images.pokemontcg.io/sm1/71.png",
                "https://images.pokemontcg.io/sm1/97.png",
                "https://images.pokemontcg.io/sm3/86.png",
                "https://images.pokemontcg.io/sm2/91.png",
                "https://images.pokemontcg.io/sm2/84.png",
                "https://images.pokemontcg.io/sm1/21.png",
                "https://images.pokemontcg.io/sm2/42.png",
                "https://images.pokemontcg.io/sm3/33.png",
                "https://images.pokemontcg.io/sm3/54.png",
                "https://images.pokemontcg.io/sm1/63.png",
                "https://images.pokemontcg.io/sm2/96.png",
                "https://images.pokemontcg.io/sm3/104.png",
                "https://images.pokemontcg.io/sm1/80.png",
                "https://images.pokemontcg.io/sm2/90.png",
                "https://images.pokemontcg.io/sm1/50.png",
                "https://images.pokemontcg.io/sm2/139.png",
                "https://images.pokemontcg.io/sm2/20.png",
                "https://images.pokemontcg.io/sm3/16.png",
                "https://images.pokemontcg.io/sm1/102.png",
                "https://images.pokemontcg.io/sm3/132.png",
                "https://images.pokemontcg.io/sm3/118.png",
                "https://images.pokemontcg.io/sm1/32.png",
                "https://images.pokemontcg.io/sm3/112.png",
                "https://images.pokemontcg.io/sm2/11.png",
                "https://images.pokemontcg.io/sm3/44.png",
                "https://images.pokemontcg.io/sm3/87.png",
                "https://images.pokemontcg.io/sm2/94.png",
                "https://images.pokemontcg.io/sm1/3.png",
                "https://images.pokemontcg.io/sm1/28.png",
                "https://images.pokemontcg.io/sm2/97.png",
                "https://images.pokemontcg.io/sm2/80.png",
                "https://images.pokemontcg.io/sm2/105.png",
                "https://images.pokemontcg.io/sm3/106.png",
                "https://images.pokemontcg.io/sm3/116.png",
                "https://images.pokemontcg.io/sm1/9.png",
                "https://images.pokemontcg.io/sm1/68.png",
                "https://images.pokemontcg.io/sm2/69.png",
                "https://images.pokemontcg.io/sm3/81.png",
                "https://images.pokemontcg.io/sm2/102.png",
                "https://images.pokemontcg.io/sm3/30.png",
                "https://images.pokemontcg.io/sm2/142.png",
                "https://images.pokemontcg.io/sm1/146.png",
                "https://images.pokemontcg.io/sm3/9.png",
                "https://images.pokemontcg.io/sm3/2.png",
                "https://images.pokemontcg.io/sm1/110.png",
                "https://images.pokemontcg.io/sm3/107.png",
                "https://images.pokemontcg.io/sm3/57.png",
                "https://images.pokemontcg.io/sm1/76.png",
                "https://images.pokemontcg.io/sm3/89.png",
                "https://images.pokemontcg.io/sm2/30.png",
                "https://images.pokemontcg.io/sm1/16.png",
                "https://images.pokemontcg.io/sm2/24.png",
                "https://images.pokemontcg.io/sm2/115.png",
                "https://images.pokemontcg.io/sm3/65.png",
                "https://images.pokemontcg.io/sm3/64.png",
                "https://images.pokemontcg.io/sm1/47.png",
                "https://images.pokemontcg.io/sm2/61.png",
                "https://images.pokemontcg.io/sm3/50.png",
                "https://images.pokemontcg.io/sm3/24.png",
                "https://images.pokemontcg.io/sm3/55.png",
                "https://images.pokemontcg.io/sm1/89.png",
                "https://images.pokemontcg.io/sm1/82.png",
                "https://images.pokemontcg.io/sm3/26.png",
                "https://images.pokemontcg.io/sm1/109.png",
                "https://images.pokemontcg.io/sm1/65.png",
                "https://images.pokemontcg.io/sm3/20.png",
                "https://images.pokemontcg.io/sm3/35.png",
                "https://images.pokemontcg.io/sm1/78.png",
                "https://images.pokemontcg.io/sm2/112.png",
                "https://images.pokemontcg.io/sm3/83.png",
                "https://images.pokemontcg.io/sm3/8.png",
                "https://images.pokemontcg.io/sm2/89.png",
                "https://images.pokemontcg.io/sm1/57.png",
                "https://images.pokemontcg.io/sm2/9.png",
                "https://images.pokemontcg.io/sm2/27.png",
                "https://images.pokemontcg.io/sm1/72.png",
                "https://images.pokemontcg.io/sm1/14.png",
                "https://images.pokemontcg.io/sm1/85.png",
                "https://images.pokemontcg.io/sm3/119.png",
                "https://images.pokemontcg.io/sm3/113.png",
                "https://images.pokemontcg.io/sm3/31.png",
                "https://images.pokemontcg.io/sm3/18.png",
                "https://images.pokemontcg.io/sm3/139.png",
                "https://images.pokemontcg.io/sm2/114.png",
                "https://images.pokemontcg.io/sm3/37.png",
                "https://images.pokemontcg.io/sm1/95.png",
                "https://images.pokemontcg.io/sm2/73.png",
                "https://images.pokemontcg.io/sm2/15.png",
                "https://images.pokemontcg.io/sm2/66.png",
                "https://images.pokemontcg.io/sm3/75.png",
                "https://images.pokemontcg.io/sm2/129.png",
                "https://images.pokemontcg.io/sm2/104.png",
                "https://images.pokemontcg.io/sm3/32.png",
                "https://images.pokemontcg.io/sm2/106.png",
                "https://images.pokemontcg.io/sm3/62.png",
                "https://images.pokemontcg.io/sm2/111.png",
                "https://images.pokemontcg.io/sm2/95.png",
                "https://images.pokemontcg.io/sm1/7.png",
                "https://images.pokemontcg.io/sm3/85.png",
                "https://images.pokemontcg.io/sm3/141.png",
                "https://images.pokemontcg.io/sm3/147.png",
                "https://images.pokemontcg.io/sm3/27.png",
                "https://images.pokemontcg.io/sm3/79.png",
                "https://images.pokemontcg.io/sm3/142.png",
                "https://images.pokemontcg.io/sm1/126.png",
                "https://images.pokemontcg.io/sm2/109.png",
                "https://images.pokemontcg.io/sm2/3.png",
                "https://images.pokemontcg.io/sm3/100.png",
                "https://images.pokemontcg.io/sm1/37.png",
                "https://images.pokemontcg.io/sm2/22.png",
                "https://images.pokemontcg.io/sm1/4.png",
                "https://images.pokemontcg.io/sm2/134.png",
                "https://images.pokemontcg.io/sm1/118.png",
                "https://images.pokemontcg.io/sm1/59.png",
                "https://images.pokemontcg.io/sm1/64.png",
                "https://images.pokemontcg.io/sm1/125.png",
                "https://images.pokemontcg.io/sm3/45.png",
                "https://images.pokemontcg.io/sm3/115.png",
                "https://images.pokemontcg.io/sm1/94.png",
                "https://images.pokemontcg.io/sm1/90.png",
                "https://images.pokemontcg.io/sm2/76.png",
                "https://images.pokemontcg.io/sm1/56.png",
                "https://images.pokemontcg.io/sm1/29.png",
                "https://images.pokemontcg.io/sm1/45.png",
                "https://images.pokemontcg.io/sm2/19.png",
                "https://images.pokemontcg.io/sm1/33.png",
                "https://images.pokemontcg.io/sm2/100.png",
                "https://images.pokemontcg.io/sm3/7.png",
                "https://images.pokemontcg.io/sm2/138.png",
                "https://images.pokemontcg.io/sm3/76.png",
                "https://images.pokemontcg.io/sm1/86.png",
                "https://images.pokemontcg.io/sm1/123.png",
                "https://images.pokemontcg.io/sm2/35.png",
                "https://images.pokemontcg.io/sm1/135.png",
                "https://images.pokemontcg.io/sm2/75.png",
                "https://images.pokemontcg.io/sm2/120.png",
                "https://images.pokemontcg.io/sm1/60.png",
                "https://images.pokemontcg.io/sm1/92.png",
                "https://images.pokemontcg.io/sm3/17.png",
                "https://images.pokemontcg.io/sm2/87.png",
                "https://images.pokemontcg.io/sm2/123.png",
                "https://images.pokemontcg.io/sm2/38.png",
                "https://images.pokemontcg.io/sm1/11.png",
                "https://images.pokemontcg.io/sm3/23.png",
                "https://images.pokemontcg.io/sm3/122.png",
                "https://images.pokemontcg.io/sm2/4.png",
                "https://images.pokemontcg.io/sm3/47.png",
                "https://images.pokemontcg.io/sm2/77.png",
                "https://images.pokemontcg.io/sm1/2.png",
                "https://images.pokemontcg.io/sm1/113.png",
                "https://images.pokemontcg.io/sm1/130.png",
                "https://images.pokemontcg.io/sm3/121.png",
                "https://images.pokemontcg.io/sm3/97.png",
                "https://images.pokemontcg.io/sm3/93.png",
                "https://images.pokemontcg.io/sm3/109.png",
                "https://images.pokemontcg.io/sm1/58.png",
                "https://images.pokemontcg.io/sm3/3.png",
                "https://images.pokemontcg.io/sm3/138.png",
                "https://images.pokemontcg.io/sm3/99.png",
                "https://images.pokemontcg.io/sm2/1.png",
                "https://images.pokemontcg.io/sm1/40.png",
                "https://images.pokemontcg.io/sm1/138.png",
                "https://images.pokemontcg.io/sm3/25.png",
                "https://images.pokemontcg.io/sm1/98.png",
                "https://images.pokemontcg.io/sm3/80.png",
                "https://images.pokemontcg.io/sm2/55.png",
                "https://images.pokemontcg.io/sm1/91.png",
                "https://images.pokemontcg.io/sm2/83.png",
                "https://images.pokemontcg.io/sm1/83.png",
                "https://images.pokemontcg.io/sm3/43.png",
                "https://images.pokemontcg.io/sm3/15.png",
                "https://images.pokemontcg.io/sm2/21.png",
                "https://images.pokemontcg.io/sm1/61.png",
                "https://images.pokemontcg.io/sm1/53.png",
                "https://images.pokemontcg.io/sm2/68.png",
                "https://images.pokemontcg.io/sm1/117.png",
                "https://images.pokemontcg.io/sm2/52.png",
                "https://images.pokemontcg.io/sm1/88.png",
                "https://images.pokemontcg.io/sm3/73.png",
                "https://images.pokemontcg.io/sm1/134.png",
                "https://images.pokemontcg.io/sm1/148.png",
                "https://images.pokemontcg.io/sm3/58.png",
                "https://images.pokemontcg.io/sm1/23.png",
                "https://images.pokemontcg.io/sm1/144.png",
                "https://images.pokemontcg.io/sm2/12.png",
                "https://images.pokemontcg.io/sm3/120.png",
                "https://images.pokemontcg.io/sm1/10.png",
                "https://images.pokemontcg.io/sm2/108.png",
                "https://images.pokemontcg.io/sm2/5.png",
                "https://images.pokemontcg.io/sm3/52.png",
                "https://images.pokemontcg.io/sm2/28.png",
                "https://images.pokemontcg.io/sm2/71.png",
                "https://images.pokemontcg.io/sm3/63.png",
                "https://images.pokemontcg.io/sm1/25.png",
                "https://images.pokemontcg.io/sm1/22.png",
                "https://images.pokemontcg.io/sm3/61.png",
                "https://images.pokemontcg.io/sm3/40.png",
                "https://images.pokemontcg.io/sm2/18.png",
                "https://images.pokemontcg.io/sm2/118.png",
                "https://images.pokemontcg.io/sm1/75.png",
                "https://images.pokemontcg.io/sm1/73.png",
                "https://images.pokemontcg.io/sm1/70.png",
                "https://images.pokemontcg.io/sm3/125.png",
                "https://images.pokemontcg.io/sm1/18.png",
                "https://images.pokemontcg.io/sm1/77.png",
                "https://images.pokemontcg.io/sm3/126.png",
                "https://images.pokemontcg.io/sm3/84.png",
                "https://images.pokemontcg.io/sm2/53.png",
                "https://images.pokemontcg.io/sm3/96.png",
                "https://images.pokemontcg.io/sm3/108.png",
                "https://images.pokemontcg.io/sm2/34.png",
                "https://images.pokemontcg.io/sm1/17.png",
                "https://images.pokemontcg.io/sm2/125.png",
                "https://images.pokemontcg.io/sm3/66.png",
                "https://images.pokemontcg.io/sm3/140.png",
                "https://images.pokemontcg.io/sm1/13.png",
                "https://images.pokemontcg.io/sm2/41.png",
                "https://images.pokemontcg.io/sm1/30.png",
                "https://images.pokemontcg.io/sm1/15.png",
                "https://images.pokemontcg.io/sm1/99.png",
                "https://images.pokemontcg.io/sm3/114.png",
                "https://images.pokemontcg.io/sm2/126.png",
                "https://images.pokemontcg.io/sm2/54.png",
                "https://images.pokemontcg.io/sm3/105.png",
                "https://images.pokemontcg.io/sm3/103.png",
                "https://images.pokemontcg.io/sm1/79.png",
                "https://images.pokemontcg.io/sm1/87.png",
                "https://images.pokemontcg.io/sm2/110.png",
                "https://images.pokemontcg.io/sm3/129.png",
                "https://images.pokemontcg.io/sm3/14.png",
                "https://images.pokemontcg.io/sm1/147.png",
                "https://images.pokemontcg.io/sm3/1.png",
                "https://images.pokemontcg.io/sm1/31.png",
                "https://images.pokemontcg.io/sm1/108.png",
                "https://images.pokemontcg.io/sm2/8.png",
                "https://images.pokemontcg.io/sm3/4.png",
                "https://images.pokemontcg.io/sm2/92.png",
                "https://images.pokemontcg.io/sm3/123.png",
                "https://images.pokemontcg.io/sm2/101.png",
                "https://images.pokemontcg.io/sm2/144.png",
                "https://images.pokemontcg.io/sm2/135.png",
                "https://images.pokemontcg.io/sm3/67.png",
                "https://images.pokemontcg.io/sm1/43.png",
                "https://images.pokemontcg.io/sm2/117.png",
                "https://images.pokemontcg.io/sm3/134.png",
                "https://images.pokemontcg.io/sm1/48.png",
                "https://images.pokemontcg.io/sm2/31.png",
                "https://images.pokemontcg.io/sm2/86.png",
                "https://images.pokemontcg.io/sm2/64.png",
                "https://images.pokemontcg.io/sm1/51.png",
                "https://images.pokemontcg.io/sm2/49.png",
                "https://images.pokemontcg.io/sm3/94.png",
                "https://images.pokemontcg.io/sm1/139.png",
                "https://images.pokemontcg.io/sm3/59.png",
                "https://images.pokemontcg.io/sm2/78.png",
                "https://images.pokemontcg.io/sm2/93.png",
                "https://images.pokemontcg.io/sm2/10.png",
                "https://images.pokemontcg.io/sm3/39.png",
                "https://images.pokemontcg.io/sm3/131.png",
                "https://images.pokemontcg.io/sm3/12.png",
                "https://images.pokemontcg.io/sm2/122.png",
                "https://images.pokemontcg.io/sm1/112.png",
                "https://images.pokemontcg.io/sm2/140.png",
                "https://images.pokemontcg.io/sm2/2.png",
                "https://images.pokemontcg.io/sm3/136.png",
                "https://images.pokemontcg.io/sm2/119.png",
                "https://images.pokemontcg.io/sm1/114.png",
                "https://images.pokemontcg.io/sm2/130.png",
                "https://images.pokemontcg.io/sm2/81.png",
                "https://images.pokemontcg.io/sm1/12.png",
                "https://images.pokemontcg.io/sm3/11.png",
                "https://images.pokemontcg.io/sm2/82.png",
                "https://images.pokemontcg.io/sm2/26.png",
                "https://images.pokemontcg.io/sm1/141.png",
                "https://images.pokemontcg.io/sm2/116.png",
                "https://images.pokemontcg.io/sm2/133.png",
                "https://images.pokemontcg.io/sm1/81.png",
                "https://images.pokemontcg.io/sm1/74.png",
                "https://images.pokemontcg.io/sm1/49.png",
                "https://images.pokemontcg.io/sm3/144.png",
                "https://images.pokemontcg.io/sm1/124.png",
                "https://images.pokemontcg.io/sm2/50.png",
                "https://images.pokemontcg.io/sm2/47.png",
                "https://images.pokemontcg.io/sm1/129.png",
                "https://images.pokemontcg.io/sm2/113.png",
                "https://images.pokemontcg.io/sm1/136.png",
                "https://images.pokemontcg.io/sm2/79.png",
                "https://images.pokemontcg.io/sm3/72.png",
                "https://images.pokemontcg.io/sm3/70.png",
                "https://images.pokemontcg.io/sm2/136.png",
                "https://images.pokemontcg.io/sm3/146.png",
                "https://images.pokemontcg.io/sm3/92.png",
                "https://images.pokemontcg.io/sm2/65.png",
                "https://images.pokemontcg.io/sm2/128.png",
                "https://images.pokemontcg.io/sm2/33.png",
                "https://images.pokemontcg.io/sm1/140.png",
                "https://images.pokemontcg.io/sm2/48.png",
                "https://images.pokemontcg.io/sm3/46.png",
                "https://images.pokemontcg.io/sm1/39.png",
                "https://images.pokemontcg.io/sm2/132.png",
                "https://images.pokemontcg.io/sm1/116.png",
                "https://images.pokemontcg.io/sm3/101.png",
                "https://images.pokemontcg.io/sm1/96.png",
                "https://images.pokemontcg.io/sm2/70.png",
                "https://images.pokemontcg.io/sm1/115.png",
                "https://images.pokemontcg.io/sm1/52.png",
                "https://images.pokemontcg.io/sm2/6.png",
                "https://images.pokemontcg.io/sm1/36.png",
                "https://images.pokemontcg.io/sm2/88.png",
                "https://images.pokemontcg.io/sm2/14.png",
                "https://images.pokemontcg.io/sm2/37.png",
                "https://images.pokemontcg.io/sm2/72.png",
                "https://images.pokemontcg.io/sm3/6.png",
                "https://images.pokemontcg.io/sm1/44.png",
                "https://images.pokemontcg.io/sm3/34.png",
                "https://images.pokemontcg.io/sm2/23.png",
                "https://images.pokemontcg.io/sm2/51.png",
                "https://images.pokemontcg.io/sm2/25.png",
                "https://images.pokemontcg.io/sm1/1.png",
                "https://images.pokemontcg.io/sm2/17.png",
                "https://images.pokemontcg.io/sm3/133.png",
                "https://images.pokemontcg.io/sm3/60.png",
                "https://images.pokemontcg.io/sm3/13.png",
                "https://images.pokemontcg.io/sm1/127.png",
                "https://images.pokemontcg.io/sm3/102.png",
                "https://images.pokemontcg.io/sm2/46.png",
                "https://images.pokemontcg.io/sm1/142.png",
                "https://images.pokemontcg.io/sm1/8.png",
                "https://images.pokemontcg.io/sm3/19.png",
                "https://images.pokemontcg.io/sm3/127.png",
                "https://images.pokemontcg.io/sm3/69.png",
                "https://images.pokemontcg.io/sm3/135.png",
                "https://images.pokemontcg.io/sm2/124.png",
                "https://images.pokemontcg.io/sm1/145.png",
                "https://images.pokemontcg.io/sm3/28.png",
                "https://images.pokemontcg.io/sm1/41.png",
                "https://images.pokemontcg.io/sm2/143.png",
                "https://images.pokemontcg.io/sm2/137.png",
                "https://images.pokemontcg.io/sm1/19.png",
                "https://images.pokemontcg.io/sm3/91.png",
                "https://images.pokemontcg.io/sm1/38.png",
                "https://images.pokemontcg.io/sm1/54.png",
                "https://images.pokemontcg.io/sm2/16.png",
                "https://images.pokemontcg.io/sm1/34.png",
                "https://images.pokemontcg.io/sm1/149.png",
                "https://images.pokemontcg.io/sm1/120.png",
                "https://images.pokemontcg.io/sm3/143.png",
                "https://images.pokemontcg.io/sm1/66.png",
                "https://images.pokemontcg.io/sm2/85.png",
                "https://images.pokemontcg.io/sm1/67.png",
                "https://images.pokemontcg.io/sm1/55.png",
                "https://images.pokemontcg.io/sm3/128.png",
                "https://images.pokemontcg.io/sm2/58.png",
                "https://images.pokemontcg.io/sm3/110.png",
                "https://images.pokemontcg.io/sm2/145.png",
                "https://images.pokemontcg.io/sm3/53.png",
                "https://images.pokemontcg.io/sm2/7.png",
                "https://images.pokemontcg.io/sm3/88.png",
                "https://images.pokemontcg.io/sm1/131.png",
                "https://images.pokemontcg.io/sm3/78.png",
                "https://images.pokemontcg.io/sm1/105.png",
                "https://images.pokemontcg.io/sm1/26.png",
                "https://images.pokemontcg.io/sm3/137.png",
                "https://images.pokemontcg.io/sm3/68.png",
                "https://images.pokemontcg.io/sm3/49.png",
                "https://images.pokemontcg.io/sm2/127.png",
                "https://images.pokemontcg.io/sm3/74.png",
                "https://images.pokemontcg.io/sm1/6.png",
                "https://images.pokemontcg.io/sm3/36.png",
                "https://images.pokemontcg.io/sm3/130.png",
                "https://images.pokemontcg.io/sm3/82.png",
                "https://images.pokemontcg.io/sm2/74.png",
                "https://images.pokemontcg.io/sm3/145.png",
                "https://images.pokemontcg.io/sm1/42.png",
                "https://images.pokemontcg.io/sm3/10.png",
                "https://images.pokemontcg.io/sm3/111.png",
                "https://images.pokemontcg.io/sm1/20.png",
                "https://images.pokemontcg.io/sm1/27.png"
        )
    }
}