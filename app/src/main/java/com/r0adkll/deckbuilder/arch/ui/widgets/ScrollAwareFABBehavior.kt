package com.r0adkll.deckbuilder.arch.ui.widgets


import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.kotlin.extensions.dpToPx


@Suppress("UNUSED_PARAMETER")
class ScrollAwareFABBehavior(
        context: Context,
        attrs: AttributeSet
) : CoordinatorLayout.Behavior<FloatingActionButton>() {

    private val maxTransY = context.dpToPx(56f + 16f)


    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     nestedScrollAxes: Int,
                                     nestedScrollType: Int): Boolean {
        return true
    }


    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton,
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
