package com.r0adkll.deckbuilder.util.extensions

import com.ftinc.kit.widget.EmptyView

fun EmptyView.setLoading(enabled: Boolean) {
    state = if (enabled) {
        EmptyView.State.LOADING
    } else {
        EmptyView.State.EMPTY
    }
}
