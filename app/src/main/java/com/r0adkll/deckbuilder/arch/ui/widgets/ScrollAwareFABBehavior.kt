package com.r0adkll.deckbuilder.arch.ui.widgets


import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.kotlin.extensions.dpToPx


@Suppress("UNUSED_PARAMETER")
class ScrollAwareFABBehavior(
        context: Context,
        attrs: AttributeSet
) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<com.google.android.material.floatingactionbutton.FloatingActionButton>() {

    private val maxTransY = context.dpToPx(56f + 16f)


    override fun onStartNestedScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
                                     child: com.google.android.material.floatingactionbutton.FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     nestedScrollAxes: Int,
                                     nestedScrollType: Int): Boolean {
        return true
    }


    override fun onNestedScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
                                child: com.google.android.material.floatingactionbutton.FloatingActionButton,
                                target: View,
                                dxConsumed: Int, dyConsumed: Int,
                                dxUnconsumed: Int, dyUnconsumed: Int,
                                nestedScrollType: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, nestedScrollType)

        val dY = when {
            dyConsumed != 0 -> dyConsumed
            child.translationY != 0f -> dyUnconsumed
            else -> 0
        }

        val transY = (child.translationY + dY).coerceIn(0f..maxTransY)
        child.translationY = transY
    }
}
