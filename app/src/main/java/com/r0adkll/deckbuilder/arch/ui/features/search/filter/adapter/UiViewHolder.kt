package com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter


import android.content.res.ColorStateList
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.jakewharton.rxrelay2.Relay
import com.nex3z.flowlayout.FlowLayout
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterIntentions
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi.FilterAttribute
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item.ValueRange.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item.ValueRange.Modifier.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.UiViewHolder.ViewType.*
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonTypeView
import com.r0adkll.deckbuilder.arch.ui.widgets.SeekBarIndicatorView
import com.r0adkll.deckbuilder.util.bindView
import com.r0adkll.deckbuilder.util.bindViews
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import timber.log.Timber


sealed class UiViewHolder<in I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)


    /**
     * Recycler UI Item for [Item.Header]
     */
    class HeaderViewHolder(itemView: View) : UiViewHolder<Item.Header>(itemView) {

        private val title: TextView by bindView(R.id.title)


        override fun bind(item: Item.Header) {
            title.setText(item.title)
        }
    }


    /**
     * Recycler UI Item for [Item.Type]
     */
    class TypeViewHolder(
            itemView: View,
            private val typeClicks: Relay<Pair<String, io.pokemontcg.model.Type>>
    ) : UiViewHolder<Item.Type>(itemView) {

        private val types: List<PokemonTypeView> by bindViews(
                R.id.type_colorless,
                R.id.type_fire,
                R.id.type_grass,
                R.id.type_water,
                R.id.type_electric,
                R.id.type_fighting,
                R.id.type_psychic,
                R.id.type_steel,
                R.id.type_dragon,
                R.id.type_fairy,
                R.id.type_dark
        )


        override fun bind(item: Item.Type) {
            types.forEach { it.checked = false }
            item.selected.forEach { selectType(it) }

            types.forEach {
                it.setOnClickListener {
                    val value = Pair(item.key, (it as PokemonTypeView).type)
                    typeClicks.accept(value)
                }
            }
        }


        private fun selectType(type: Type) {
            types.find { it.type == type}?.checked = true
        }
    }


    /**
     * Recycler UI Item for [Item.Attribute]
     */
    class AttributesViewHolder(
            itemView: View,
            private val attributeClicks: Relay<FilterAttribute>
    ) : UiViewHolder<Item.Attribute>(itemView) {

        private val inflater: LayoutInflater = LayoutInflater.from(itemView.context)
        private val container: FlowLayout by bindView(R.id.attribute_container)


        override fun bind(item: Item.Attribute) {
            container.removeAllViews()
            item.attributes.forEach { attr ->
                val isChecked = item.selected.contains(attr)
                val view = inflater.inflate(R.layout.item_attribute, container, false) as CheckedTextView
                view.text = when(attr) {
                    is FilterAttribute.SubTypeAttribute -> attr.subType.displayName
                    is FilterAttribute.ContainsAttribute -> attr.attribute
                }
                view.setTextColor(color(if (isChecked) R.color.white else R.color.black87))
                view.elevation = if (isChecked) dpToPx(4f) else 0f
                view.isChecked = isChecked
                view.setOnClickListener {
                    attributeClicks.accept(attr)
                }

                container.addView(view)
            }
        }
    }


    /**
     * Recycler UI Item for [Item.Option]
     */
    class OptionViewHolder(
            itemView: View,
            private val optionClicks: Relay<Pair<String, Any>>
    ) : UiViewHolder<Item.Option<*>>(itemView) {

        private val icon: ImageView by bindView(R.id.icon)
        private val text: TextView by bindView(R.id.title)
        private val checkBox: CheckBox by bindView(R.id.checkbox)


        override fun bind(item: Item.Option<*>) {
            itemView.setOnClickListener {
                optionClicks.accept(Pair(item.key, item.option!!))
            }
            checkBox.isChecked = item.isSelected
            val opt = item.option
            when(opt) {
                is Expansion -> {
                    text.text = opt.name
                    GlideApp.with(itemView)
                            .load(opt.symbolUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(icon)
                }
                is Rarity -> {
                    text.text = opt.key
                    icon.setImageResource(when(opt){
                        Rarity.COMMON -> R.drawable.ic_rarity_common
                        Rarity.UNCOMMON -> R.drawable.ic_rarity_uncommon
                        Rarity.RARE -> R.drawable.ic_rarity_rare
                        Rarity.HOLO_RARE -> R.drawable.ic_rarity_holo
                        else -> R.drawable.ic_rarity_holo
                    })
                }
            }
        }
    }


    /**
     * Recycler UI Item for [Item.ViewMore]
     */
    class ViewMoreViewHolder(
            itemView: View,
            private val viewMoreClicks: Relay<Unit>
    ) : UiViewHolder<Item.ViewMore>(itemView) {

        private val text: TextView by bindView(android.R.id.text1)


        override fun bind(item: Item.ViewMore) {
            text.setText(item.title)
            itemView.setOnClickListener { viewMoreClicks.accept(Unit) }
        }
    }



    class ValueRangeViewHolder(
            itemView: View,
            private val valueRangeChange: Relay<Pair<String, Item.ValueRange.Value>>
    ) : UiViewHolder<Item.ValueRange>(itemView) {

        private val seekBar: SeekBar by bindView(R.id.seekBar)
        private val seekBarIndicator: SeekBarIndicatorView by bindView(R.id.seekBarIndicator)
        private val modifiers: List<ImageView> by bindViews(
                R.id.modifier_greater_than,
                R.id.modifier_greater_than_equal,
                R.id.modifier_less_than,
                R.id.modifier_less_than_equal
        )


        override fun bind(item: Item.ValueRange) {
            seekBar.setOnSeekBarChangeListener(seekBarIndicator)

            seekBar.max = item.max
            seekBar.progress = item.value.value

            modifiers.forEach {
                it.select(it.id == item.value.modifier.viewId())

                it.setOnClickListener {
                    var modifier = it.id.modifier()
                    val progress = seekBar.progress

                    // "Uncheck" the modifier if the user clicks it again
                    if (item.value.modifier == modifier) {
                        modifier = NONE
                    }

                    Timber.i("Modifier Clicked(${item.key}, progress: $progress, modifier: ${modifier.name})")
                    valueRangeChange.accept(Pair(item.key, Value(progress, modifier)))
                }
            }

            seekBarIndicator.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                }


                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }


                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val modifier = modifiers.firstOrNull { it.alpha == 1f }?.id?.modifier() ?: NONE
                    valueRangeChange.accept(Pair(item.key, Value(seekBar.progress, modifier)))
                }
            })
        }


        private fun Modifier.viewId(): Int = when(this) {
            NONE -> -1
            GREATER_THAN -> R.id.modifier_greater_than
            GREATER_THAN_EQUALS -> R.id.modifier_greater_than_equal
            LESS_THAN -> R.id.modifier_less_than
            LESS_THAN_EQUALS -> R.id.modifier_less_than_equal
        }


        private fun Int.modifier(): Modifier = when(this) {
            R.id.modifier_greater_than -> GREATER_THAN
            R.id.modifier_greater_than_equal -> GREATER_THAN_EQUALS
            R.id.modifier_less_than -> LESS_THAN
            R.id.modifier_less_than_equal -> LESS_THAN_EQUALS
            else -> NONE
        }


        private fun ImageView.select(isSelected: Boolean) {
            this.alpha = if (isSelected) 1f else 0.26f
            this.imageTintList = ColorStateList.valueOf(color(
                    if (isSelected) R.color.primaryColor else R.color.black
            ))
        }
    }


    private enum class ViewType(@LayoutRes val layoutId: Int) {
        HEADER(R.layout.item_filter_header),
        TYPE(R.layout.item_filter_types),
        ATTRIBUTES(R.layout.item_filter_attributes),
        OPTION(R.layout.item_filter_option),
        VIEW_MORE(R.layout.item_filter_view_more),
        VALUE_RANGE(R.layout.item_filter_value_range);

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
        fun create(itemView: View,
                   layoutId: Int,
                   intentions: FilterIntentions): UiViewHolder<Item> {
            val viewType = ViewType.of(layoutId)
            return when(viewType) {
                HEADER -> HeaderViewHolder(itemView) as UiViewHolder<Item>
                TYPE -> TypeViewHolder(itemView, intentions.typeClicks) as UiViewHolder<Item>
                ATTRIBUTES -> AttributesViewHolder(itemView, intentions.attributeClicks) as UiViewHolder<Item>
                OPTION -> OptionViewHolder(itemView, intentions.optionClicks) as UiViewHolder<Item>
                VIEW_MORE -> ViewMoreViewHolder(itemView, intentions.viewMoreClicks) as UiViewHolder<Item>
                VALUE_RANGE -> ValueRangeViewHolder(itemView, intentions.valueRangeChanges) as UiViewHolder<Item>
            }
        }
    }
}