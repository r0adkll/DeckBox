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
import com.r0adkll.deckbuilder.util.extensions.pokemon
import com.r0adkll.deckbuilder.util.glide.EnergyCropTransformation
import com.r0adkll.deckbuilder.util.glide.ToolCropTransformation
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import java.util.*


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

//        if (isInEditMode) {
            val poke = pokemon {
                id = "sm8-205"
                name = "Alolan Ninetales-GX"
                nationalPokedexNumber = 38
                hp = 200
                imageUrl = "https://images.pokemontcg.io/sm8/205.png"
                imageUrlHiRes = "https://images.pokemontcg.io/sm8/205_hires.png"
            }

            val energy = pokemon {
                id = "sm1-171"
                name = "Fairy Energy"
                supertype = SuperType.ENERGY
                subtype = SubType.BASIC
                types = listOf(Type.FAIRY)
            }

            val tool = pokemon {
                id = "sm8-190"
                name = "Spell Tag"
                imageUrl = "https://images.pokemontcg.io/sm8/190.png"
                imageUrlHiRes = "https://images.pokemontcg.io/sm8/190_hires.png"
                supertype = SuperType.TRAINER
                subtype = SubType.POKEMON_TOOL
            }


            card = Board.Card(ArrayDeque(listOf(poke)),
                    listOf(energy, energy.copy(), energy.copy()),
                    listOf(tool),
                    false, false, null, 100)
//        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Layout image
        image.layout(l, t, r, b)

        // Layout damage
        val damageLeft = (r - l) - (damage.measuredWidth / 2)
        val damageTop = -(damage.measuredHeight / 2)
        damage.layout(damageLeft, damageTop, damageLeft + damage.measuredWidth, damageTop + damage.measuredHeight)

        // Layout Tools
        tools.forEachIndexed { index, view ->
            val toolX = r - (view.measuredWidth / 2)
            val toolY = t + dipToPx(36f + 8f) + (index * view.measuredHeight) + (index * dipToPx(4f))
            view.layout(toolX, toolY, toolX + view.measuredWidth, toolY + view.measuredHeight)
        }

        // Layout Energies
        energies.forEachIndexed { index, view ->
            val energyX = l + dipToPx(4f) + (index * view.measuredWidth) + (index * dipToPx(4f))
            val energyY = b - (view.measuredHeight / 2)
            view.layout(energyX, energyY, energyX + view.measuredWidth, energyY + view.measuredHeight)
        }
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