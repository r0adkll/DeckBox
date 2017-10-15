package com.r0adkll.deckbuilder.arch.ui.components


import android.support.v7.util.DiffUtil


data class RecyclerViewBinding<out T>(
        val new: List<T>,
        val diff: DiffUtil.DiffResult
)