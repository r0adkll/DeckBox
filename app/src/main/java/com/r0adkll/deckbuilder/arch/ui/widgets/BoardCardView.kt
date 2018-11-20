package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.R


class BoardCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {



    /**
     * The [Board.Card] State that this view will be rendering
     */
    var card: Board.Card? = null

    private val energies = ArrayList<ImageView>()
    private val tools = ArrayList<ImageView>()
    private val image = BezelImageView(context)
    private val damage = TextView(context)

    init {
        image.maskDrawable = context.getDrawable(R.drawable.dr_mask_round_rect)
        val imageLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(image, imageLp)

        // setup damage textview
        damage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        damage.setTextColor(color(R.color.white))
        damage.setBackgroundResource(R.drawable.dr_damage_background)
        val pad = dipToPx(4f)
        damage.setPaddingRelative(pad, pad, pad, pad)
        damage.gravity = Gravity.CENTER

        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, dipToPx(36f))
        addView(damage, lp)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}