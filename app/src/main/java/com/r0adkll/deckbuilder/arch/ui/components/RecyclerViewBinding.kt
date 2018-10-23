package com.r0adkll.deckbuilder.arch.ui.components


import androidx.recyclerview.widget.DiffUtil


data class RecyclerViewBinding<out T>(
        val new: List<T>,
        val diff: DiffUtil.DiffResult
)