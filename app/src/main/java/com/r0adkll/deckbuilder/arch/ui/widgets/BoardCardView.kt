package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.Pools
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.setVisible
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.extensions.drawable
import com.r0adkll.deckbuilder.util.glide.EnergyCropTransformation
import com.r0adkll.deckbuilder.util.glide.ToolCropTransformation
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type


class BoardCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    /**
     * The [Board.Card] State that this view will be rendering
     */
    var card: Board.Card? = null
        set(value) {
            field = value
            balanceAttachmentViews()
        }

    private val pool = Pools.SimplePool<ImageView>(10)
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
        val damageLp = LayoutParams(LayoutParams.WRAP_CONTENT, dipToPx(36f))
        addView(damage, damageLp)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }


    private fun balanceAttachmentViews() {
        card?.let { c ->
            // Load pokemon on top of stack's image into the image view
            GlideApp.with(this)
                    .load(c.pokemons.peek()?.imageUrl)
                    .into(image)

            // Balance tools
            trimViews(c.tools.size, tools)
            c.tools.forEachIndexed { index, pokemonCard ->
                if (index < tools.size) {
                    val oldImage = tools[index]
                    applyToolCard(pokemonCard, oldImage)
                } else {
                    applyToolCard(pokemonCard)
                }
            }

            // Balance energy cards
            trimViews(c.energy.size, energies)
            c.energy.forEachIndexed { index, pokemonCard ->
                if (index < energies.size) {
                    val oldImage = energies[index]
                    applyEnergyCard(pokemonCard, oldImage)
                } else {
                    applyEnergyCard(pokemonCard)
                }
            }

            // Damage
            damage.setVisible(c.damage > 0)
            damage.text = "${c.damage}"
        }
    }


    private fun <T : ImageView> trimViews(dataCount: Int, views: MutableList<T>) {
        val difference = views.size - dataCount
        if (difference > 0) {
            val viewsToRemove = views.subList(dataCount, views.size)
            views.removeAll(viewsToRemove)
            viewsToRemove.forEach {
                removeView(it)
                pool.release(it)
            }
        }
    }


    private fun applyToolCard(tool: PokemonCard, imageView: ImageView? = null) {
        val view = imageView ?: acquireImageView(tools)

        // Apply tool card specifics to this image view, then add it to the view
        // Matrix clip the card image into
        GlideApp.with(view)
                .load(tool.imageUrl)
                .transform(ToolCropTransformation())
                .into(view)

        addView(view, generateToolLayoutParams())
    }


    private fun applyEnergyCard(energy: PokemonCard, imageView: ImageView? = null) {
        val view = imageView ?: acquireImageView(energies)

        // Apply energy card specs to this image view and add to the parent
        when(energy.subtype) {
            SubType.BASIC -> {
                val type = energy.types?.firstOrNull() ?: Type.COLORLESS
                view.setImageResource(type.drawable())
            }
            else -> {
                // Matrix clip card image....somehow
                GlideApp.with(view)
                        .load(energy.imageUrl)
                        .transform(EnergyCropTransformation())
                        .into(view)
            }
        }

        addView(view, generateEnergyLayoutParams())
    }


    private fun acquireImageView(views: MutableList<ImageView>): ImageView {
        val view = pool.acquire() ?: ImageView(context)
        views.add(view)
        return view
    }


    private fun generateToolLayoutParams(): LayoutParams {
        return LayoutParams(dipToPx(TOOL_WIDTH_DP), dipToPx(TOOL_HEIGHT_DP))
    }


    private fun generateEnergyLayoutParams(): LayoutParams {
        return LayoutParams(dipToPx(ENERGY_SIZE_DP), dipToPx(ENERGY_SIZE_DP))
    }


    companion object {
        const val TOOL_WIDTH_DP = 128f
        const val TOOL_HEIGHT_DP = 64f
        const val ENERGY_SIZE_DP = 40f
    }
}