@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.dp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.extensions.drawable
import io.pokemontcg.model.Type
import io.pokemontcg.model.Type.COLORLESS
import io.pokemontcg.model.Type.DARKNESS
import io.pokemontcg.model.Type.DRAGON
import io.pokemontcg.model.Type.FAIRY
import io.pokemontcg.model.Type.FIGHTING
import io.pokemontcg.model.Type.FIRE
import io.pokemontcg.model.Type.GRASS
import io.pokemontcg.model.Type.LIGHTNING
import io.pokemontcg.model.Type.METAL
import io.pokemontcg.model.Type.PSYCHIC
import io.pokemontcg.model.Type.WATER

class PokemonTypeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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

    @ColorInt
    private val highlightColor: Int = color(R.color.secondaryColor)
    private val padding: Int = dip(8)
    private val highlightWidth: Int = dip(2)
    private val highlightElevation: Float = dp(6)
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        setPadding(padding, padding, padding, padding)
        outlineProvider = TypeOutlineProvider()
        paint.color = highlightColor

        val a = context.obtainStyledAttributes(attrs, R.styleable.PokemonTypeView, defStyleAttr, 0)
        val pokeType = a.getInteger(R.styleable.PokemonTypeView_pokeType, 0)
        type = when (pokeType) {
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

    override fun onDraw(canvas: Canvas?) {
        if (checked) {
            val size = minOf(measuredWidth, measuredHeight)
            val radius = (size / 2f) - (padding - highlightWidth)
            canvas?.drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius, paint)
        }
        super.onDraw(canvas)
    }

    private fun applyType() {
        setImageResource(type.drawable)
    }

    private inner class TypeOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val size = minOf(measuredWidth, measuredHeight) - (padding * 2)
            val centerX = measuredWidth / 2
            val centerY = measuredHeight / 2
            outline?.setOval(centerX - (size / 2),
                centerY - (size / 2),
                centerX + (size / 2),
                centerY + (size / 2))
        }
    }
}
