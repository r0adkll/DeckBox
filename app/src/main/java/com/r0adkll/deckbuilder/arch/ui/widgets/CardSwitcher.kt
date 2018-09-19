package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.graphics.Palette
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ViewSwitcher
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.palette.PaletteBitmapViewTarget
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit


class CardSwitcher : ViewSwitcher {

    private val card1: PokemonCardView
    private val card2: PokemonCardView

    private var paletteChangeListener: OnPaletteChangeListener? = null
    private var disposable: Disposable? = null
    private var imageIndex = 0


    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)


    init {
        setInAnimation(context, android.R.anim.slide_in_left)
        setOutAnimation(context, android.R.anim.slide_out_right)

        var lp = LayoutParams(dipToPx(400f), LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        card1 = PokemonCardView(context)
        addView(card1, lp)

        lp = LayoutParams(dipToPx(400f), LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        card2 = PokemonCardView(context)
        addView(card2, lp)

        loadNextImage(card1)
        loadNextImage(card2)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startSwitching()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable?.dispose()
    }


    fun setOnPaletteChangeListener(listener: OnPaletteChangeListener) {
        paletteChangeListener = listener
    }


    fun setOnPaletteChangeListener(listener: (Palette) -> Unit) {
        paletteChangeListener = object : OnPaletteChangeListener {
            override fun onPaletteChanged(palette: Palette) {
                listener.invoke(palette)
            }
        }
    }


    @SuppressLint("RxSubscribeOnError", "RxDefaultScheduler")
    private fun startSwitching() {
        disposable?.dispose()
        disposable = Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    loadNextImage(nextView as PokemonCardView)
                    showNext()
                }
    }


    private fun loadNextImage(view: PokemonCardView) {
        val url = getNextImageUrl()
        GlideApp.with(this)
                .`as`(PaletteBitmap::class.java)
                .load(url)
                .into(PaletteBitmapViewTarget(view, listOf(TargetPaletteAction())))

        getNextCacheImageUrl()?.let {
            GlideApp.with(this)
                    .downloadOnly()
                    .load(it)
                    .preload()
        }
    }


    private fun getNextImageUrl(): String {
        val imageUrl = SHUFFLED_CARDS[imageIndex]
        ++imageIndex
        if (imageIndex >= SHUFFLED_CARDS.size) {
            imageIndex = 0
        }
        return imageUrl.replace(".png", "_hires.png")
    }


    private fun getNextCacheImageUrl(): String? {
        return if (imageIndex + 1 >= SHUFFLED_CARDS.size) {
            null
        } else {
            SHUFFLED_CARDS[imageIndex + 1].replace(".png", "_hires.png")
        }
    }


    inner class TargetPaletteAction : PaletteBitmapViewTarget.PaletteAction {
        override fun execute(palette: Palette?) {
            palette?.let {
                paletteChangeListener?.onPaletteChanged(it)
            }
        }
    }


    interface OnPaletteChangeListener {

        fun onPaletteChanged(palette: Palette)
    }


    companion object {
        private const val INTERVAL = 6000L

        val SHUFFLED_CARDS by lazy {
            val cards = mutableListOf(*CardUtils.CARDS)
            cards.shuffle()
            cards
        }
    }
}