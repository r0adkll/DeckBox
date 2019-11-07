package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.extensions.dp
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView

@Suppress("MagicNumber")
sealed class UiViewHolder<in I : DeckImage>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I, isSelected: Boolean?)

    protected fun getElevation(isSelected: Boolean?): Int {
        return when (isSelected) {
            null -> 3
            true -> 6
            else -> 0
        }
    }

    protected fun getScale(isSelected: Boolean?): Float {
        return when (isSelected) {
            null -> 1f
            true -> 1.05f
            else -> 0.99f
        }
    }

    protected fun getAlpha(isSelected: Boolean?): Float {
        return when (isSelected) {
            null -> 1f
            true -> 1f
            else -> 0.70f
        }
    }

    class PokemonViewHolder(itemView: View) : UiViewHolder<DeckImage.Pokemon>(itemView) {

        private val pokemonCardView = itemView as PokemonCardView

        override fun bind(item: DeckImage.Pokemon, isSelected: Boolean?) {
            GlideApp.with(itemView)
                .load(item.imageUrl)
                .placeholder(R.drawable.pokemon_card_back)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(pokemonCardView)
            pokemonCardView.elevation = dp(getElevation(isSelected))
            pokemonCardView.alpha = getAlpha(isSelected)
            pokemonCardView.scaleX = getScale(isSelected)
            pokemonCardView.scaleY = getScale(isSelected)
        }
    }

    class TypeViewHolder(itemView: View) : UiViewHolder<DeckImage.Type>(itemView) {

        private val typeCardView = itemView as DeckImageView

        override fun bind(item: DeckImage.Type, isSelected: Boolean?) {
            typeCardView.primaryType = item.type1
            typeCardView.secondaryType = item.type2
            typeCardView.elevation = dp(getElevation(isSelected))
            typeCardView.alpha = getAlpha(isSelected)
            typeCardView.scaleX = getScale(isSelected)
            typeCardView.scaleY = getScale(isSelected)
        }
    }

    private enum class ViewType(@LayoutRes val layoutId: Int) {
        POKEMON(R.layout.item_deck_image_pokemon),
        TYPE(R.layout.item_deck_image_types);

        companion object {
            val VALUES by lazy { values() }

            fun of(layoutId: Int): ViewType {
                val match = VALUES.firstOrNull { it.layoutId == layoutId }
                match?.let { return match }

                throw EnumConstantNotPresentException(ViewType::class.java, "could not find view type for $layoutId")
            }
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun create(itemView: View, layoutId: Int): UiViewHolder<DeckImage> {
            val viewType = ViewType.of(layoutId)
            return when (viewType) {
                ViewType.POKEMON -> PokemonViewHolder(itemView) as UiViewHolder<DeckImage>
                ViewType.TYPE -> TypeViewHolder(itemView) as UiViewHolder<DeckImage>
            }
        }
    }
}
