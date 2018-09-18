package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.support.annotation.ColorInt
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R
import io.pokemontcg.model.Type
import io.pokemontcg.model.Type.*


class PokemonTypeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var type: Type = COLORLESS
        set(value) {
            field = value
            applyType()
        }

    var checked: Boolean = false
        set(value) {
            field = value
            elevation = if (value) highlightElevation else 0f
            invalidate()
        }


    @ColorInt private val highlightColor: Int = color(R.color.secondaryColor)
    private val padding: Int = dipToPx(8f)
    private val highlightWidth: Int = dipToPx(2f)
    private val highlightElevation: Float = dpToPx(6f)
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)


    init {
        setPadding(padding, padding, padding, padding)
        outlineProvider = TypeOutlineProvider()
        paint.color = highlightColor

        val a = context.obtainStyledAttributes(attrs, R.styleable.PokemonTypeView, defStyleAttr, 0)
        a?.let {
            val pokeType = a.getInteger(R.styleable.PokemonTypeView_pokeType, 0)
            type = when(pokeType) {
                0 -> COLORLESS
                1 -> FIRE
                2 -> GRASS
                3 -> WATER
                4 -> LIGHTNING
                5 -> FIGHTING
                6 -> PSYCHIC
                7 -> METAL
                8 -> DRAGON
                9 -> FAIRY
                10 -> DARKNESS
                else -> COLORLESS
            }

            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (checked) {
            val size = minOf(measuredWidth, measuredHeight)
            val radius = (size / 2f) - (padding - highlightWidth)
            canvas?.drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius, paint)
        }
        super.onDraw(canvas)
    }


    private fun applyType() {
        val drawable = when(type) {
            Type.COLORLESS -> R.drawable.ic_poketype_colorless
            Type.FIRE -> R.drawable.ic_poketype_fire
            Type.GRASS -> R.drawable.ic_poketype_grass
            Type.WATER -> R.drawable.ic_poketype_water
            Type.LIGHTNING -> R.drawable.ic_poketype_electric
            Type.FIGHTING -> R.drawable.ic_poketype_fighting
            Type.PSYCHIC -> R.drawable.ic_poketype_psychic
            Type.METAL -> R.drawable.ic_poketype_steel
            Type.DRAGON -> R.drawable.ic_poketype_dragon
            Type.FAIRY -> R.drawable.ic_poketype_fairy
            Type.DARKNESS -> R.drawable.ic_poketype_dark
            else -> R.drawable.ic_poketype_colorless
        }
        setImageResource(drawable)
    }


    private inner class TypeOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val size = minOf(measuredWidth, measuredHeight) - (padding * 2) //+ (if(checked) highlightWidth * 2 else 0)
            val centerX = measuredWidth / 2
            val centerY = measuredHeight / 2
            outline?.setOval(centerX - (size / 2),
                    centerY - (size / 2),
                    centerX + (size / 2),
                    centerY + (size / 2))
        }
    }
}