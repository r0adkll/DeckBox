package com.r0adkll.deckbuilder.arch.ui.components.drag

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import androidx.viewpager.widget.ViewPager
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import android.view.DragEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.color
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import io.pokemontcg.model.SuperType

class TabletDragListener(
        val dropZone: View,
        val pager: androidx.viewpager.widget.ViewPager
) : View.OnDragListener {

    private val selectedColor by lazy { dropZone.color(R.color.secondaryColor) }
    private val unselectedColor by lazy { dropZone.color(R.color.white) }

    private val background: ImageView by lazy { dropZone.findViewById<ImageView>(R.id.dropBackground) }
    private val card: ImageView by lazy { dropZone.findViewById<ImageView>(R.id.dropCard) }
    private val message: TextView by lazy { dropZone.findViewById<TextView>(R.id.dropMessage) }

    private var listener: DropListener? = null
    private var lastPage: Int = 0
    private var wiggleAnimator: ObjectAnimator? = null

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val state = event.localState as PokemonCardView.DragState

        // Only respond if the drag and drop mode is not an edit mode
        if (!state.isEdit) {
            return when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    showDropZone()
                    lastPage = pager.currentItem
                    state.view.card?.let {
                        pager.setCurrentItem(when (it.supertype) {
                            SuperType.POKEMON -> 0
                            SuperType.TRAINER -> 1
                            else -> 2
                        }, true)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    selectDropZone()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    unselectDropZone()
                    state.view.card?.let {
                        listener?.onDrop(it)
                    }
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    unselectDropZone()
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    hideDropZone()

                    if (!event.result) {
                        pager.setCurrentItem(lastPage, true)
                    }

                    true
                }
                else -> false
            }
        } else {
            return false
        }
    }

    fun setDropListener(listener: (PokemonCard) -> Unit) {
        this.listener = object : DropListener {
            override fun onDrop(card: PokemonCard) {
                listener.invoke(card)
            }
        }
    }

    private fun unselectDropZone() {
        background.setImageResource(R.drawable.dr_dropzone_background)
        card.imageTintList = null
        message.setTextColor(unselectedColor)

        wiggleAnimator?.end()
    }

    private fun selectDropZone() {
        background.setImageResource(R.drawable.dr_dropzone_background_selected)
        card.imageTintList = ColorStateList.valueOf(selectedColor)
        message.setTextColor(selectedColor)

        wiggleAnimator?.end()
        wiggleAnimator = ObjectAnimator.ofFloat(card, "rotation", 0f, WIGGLE_ROTATION, 0f, -WIGGLE_ROTATION, 0f)
        wiggleAnimator?.duration = WIGGLE_DURATION
        wiggleAnimator?.interpolator = LinearInterpolator()
        wiggleAnimator?.repeatCount = ObjectAnimator.INFINITE
        wiggleAnimator?.repeatMode = ObjectAnimator.RESTART
        wiggleAnimator?.start()
    }

    private fun showDropZone() {
        dropZone.animate()
                .alpha(1f)
                .setDuration(ANIM_DURATION)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
    }

    private fun hideDropZone() {
        dropZone.animate()
                .alpha(0f)
                .setDuration(ANIM_DURATION)
                .setInterpolator(FastOutSlowInInterpolator())
                .start()
    }

    interface DropListener {

        fun onDrop(card: PokemonCard)
    }

    companion object {
        private const val ANIM_DURATION = 200L
        private const val WIGGLE_DURATION = 500L
        private const val WIGGLE_ROTATION = 10f

        /**
         * Attach a new [TabletDragListener] to the target view to handle the
         * Drag n' Drop operation for tablet target
         */
        fun attach(target: View, pager: androidx.viewpager.widget.ViewPager): TabletDragListener {
            val listener = TabletDragListener(target, pager)
            target.setOnDragListener(listener)
            return listener
        }

        /**
         * Attach a new [TabletDragListener] to the target view to handle the
         * Drag n' Drop operation for tablet target
         */
        fun attach(target: View, pager: androidx.viewpager.widget.ViewPager, dropListener: (PokemonCard) -> Unit): TabletDragListener {
            val listener = TabletDragListener(target, pager)
            listener.setDropListener(dropListener)
            target.setOnDragListener(listener)
            return listener
        }
    }
}
