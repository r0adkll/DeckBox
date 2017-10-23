package com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonTypeView
import com.r0adkll.deckbuilder.util.bindView
import com.r0adkll.deckbuilder.util.bindViews
import io.pokemontcg.model.Type


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


    class AttributesViewHolder<T>(
            itemView: View,
            private val attributeClicks: Relay<T>
    ) : UiViewHolder<Item.Option<T>>(itemView) {

        override fun bind(item: Item.Option<T>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}