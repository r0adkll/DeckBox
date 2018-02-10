package com.r0adkll.deckbuilder.arch.ui.components


import android.support.annotation.LayoutRes


interface RecyclerItem {

    val layoutId: Int
    val viewType get() = layoutId

    fun isItemSame(new: RecyclerItem): Boolean
    fun isContentSame(new: RecyclerItem): Boolean
}