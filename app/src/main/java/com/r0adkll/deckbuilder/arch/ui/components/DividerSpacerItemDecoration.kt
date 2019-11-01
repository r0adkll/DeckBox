package com.r0adkll.deckbuilder.arch.ui.components

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State

class DividerSpacerItemDecoration(
    private val height: Float = -1f,
    private val drawFirst: Boolean = false
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (height == -1f) {
            return
        }
        if (parent.getChildAdapterPosition(view) < 1 && !drawFirst) {
            return
        }
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.top = height.toInt()
        } else {
            outRect.left = height.toInt()
            if (parent.getChildAdapterPosition(view) == parent.childCount - 1) {
                outRect.right = height.toInt()
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager?
            return layoutManager!!.orientation
        } else {
            throw IllegalStateException(
                "DividerItemDecoration can only be used with a LinearLayoutManager.")
        }
    }
}
