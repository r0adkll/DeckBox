package com.r0adkll.deckbuilder.arch.ui.components

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.kotlin.extensions.setVisible


abstract class EmptyViewListAdapter<Item, Holder : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<Item>,
    private val emptyChangeModifier: (emptyView: View, isEmpty: Boolean) -> Unit = defaultEmptyChangeModifier
) : ListAdapter<Item, Holder>(diffCallback) {

    var emptyView: View? = null

    override fun onCurrentListChanged(previousList: MutableList<Item>, currentList: MutableList<Item>) {
        if (emptyView != null) {
            emptyChangeModifier(emptyView!!, currentList.isEmpty())
        }
    }

    companion object {

        val defaultEmptyChangeModifier: (View, Boolean) -> Unit = { view, isEmpty ->
            view.setVisible(isEmpty)
        }
    }
}
