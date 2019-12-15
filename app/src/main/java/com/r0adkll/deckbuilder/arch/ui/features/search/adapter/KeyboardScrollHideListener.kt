package com.r0adkll.deckbuilder.arch.ui.features.search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.r0adkll.deckbuilder.util.ImeUtils

class KeyboardScrollHideListener(val view: View) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            ImeUtils.hideIme(view)
        }
    }
}
