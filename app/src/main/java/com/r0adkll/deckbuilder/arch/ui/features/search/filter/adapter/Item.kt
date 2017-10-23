package com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter


import android.support.annotation.StringRes
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type


sealed class Item : RecyclerItem {

    data class Header(@StringRes val title: Int) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun isContentSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override val layoutId: Int
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    }


    data class Type(
            val key: String,
            val selected: List<io.pokemontcg.model.Type>
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun isContentSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override val layoutId: Int
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    }


    data class Attributes(
            val key: String,
            val attributes: List<SubType>,
            val selected: List<SubType>
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun isContentSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override val layoutId: Int
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    }


    data class OptionList(
            val key: String,
            val options: List<Option>,
            val selected: List<Option>
    ) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun isContentSame(new: RecyclerItem): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override val layoutId: Int
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


        data class Option(
                val id: String,
                val title: String,
                val icon: String
        )
    }


    data class ValueRange(
            val key: String,
            val min: Int,
            val max: Int,
            val value: Value
    ) {

        data class Value(
                val value: Int,
                val modifier: Modifier
        )


        enum class Modifier{
            LESS_THAN,
            LESS_THAN_EQUALS,
            GREATER_THAN,
            GREATER_THAN_EQUALS
        }

    }

}