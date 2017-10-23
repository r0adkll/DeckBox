package com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter


import android.support.v7.widget.RecyclerView
import android.view.View


sealed class UiViewHolder<in I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)


    /**
     * Recycler UI Item for [Item.Header]
     */
    class HeaderViewHolder(itemView: View) : UiViewHolder<Item.Header>(itemView) {



        override fun bind(item: Item.Header) {

        }
    }



}