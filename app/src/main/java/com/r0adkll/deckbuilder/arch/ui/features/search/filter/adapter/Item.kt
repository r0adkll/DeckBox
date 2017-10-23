package com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter


import android.support.annotation.StringRes
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem
import io.pokemontcg.model.SubType


sealed class Item : RecyclerItem {

    data class Header(@StringRes val title: Int) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Header -> new.title == title
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Header -> new.title == title
            else -> false
        }


        override val layoutId: Int = R.layout.item_filter_header
    }


    data class Type(
            val key: String,
            val selected: List<io.pokemontcg.model.Type>
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Type -> new.key == key
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Type -> new == this
            else -> false
        }


        override val layoutId: Int = R.layout.item_filter_types
    }


    data class Attributes(
            val key: String,
            val attributes: List<SubType>,
            val selected: List<SubType>
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Attributes -> new.key == key
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Attributes -> new == this
            else -> false
        }


        override val layoutId: Int = R.layout.item_filter_attributes
    }


    data class Option<out T>(
            val key: String,
            val option: T,
            val isSelected: Boolean
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Option<*> -> new.key == key
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Option<*> -> new == this
            else -> false
        }


        override val layoutId: Int = R.layout.item_filter_option

    }


    data class ViewMore(@StringRes val title: Int) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is ViewMore -> new.title == title
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is ViewMore -> new.title == title
            else -> false
        }


        override val layoutId: Int = R.layout.item_filter_view_more
    }


    data class ValueRange(
            val key: String,
            val min: Int,
            val max: Int,
            val value: Value = Value(-1, Modifier.NONE)
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is ValueRange -> new.key == key
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is ValueRange -> new == this
            else -> false
        }


        override val layoutId: Int = R.layout.item_filter_value_range


        data class Value(
                val value: Int,
                val modifier: Modifier
        )


        enum class Modifier{
            NONE,
            LESS_THAN,
            LESS_THAN_EQUALS,
            GREATER_THAN,
            GREATER_THAN_EQUALS
        }

    }

}