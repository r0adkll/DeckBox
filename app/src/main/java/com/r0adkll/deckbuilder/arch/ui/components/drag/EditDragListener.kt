package com.r0adkll.deckbuilder.arch.ui.components.drag

import android.view.DragEvent
import android.view.View
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.ftinc.kit.extensions.color
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView

class EditDragListener(
    private val dropZone: View,
    dropListener: DropListener
) : View.OnDragListener {

    private val actionAdd: TextView by lazy { dropZone.findViewById<TextView>(R.id.dropZoneAdd) }
    private val actionRemove: TextView by lazy { dropZone.findViewById<TextView>(R.id.dropZoneRemove) }

    init {
        AddDragListener.attach(actionAdd, dropListener)
        RemoveDragListener.attach(actionRemove, dropListener)
    }

    override fun onDrag(v: View, event: DragEvent): Boolean {
        val state = event.localState as? PokemonCardView.DragState
        if (state != null) {
            val card = state.view.card
            return if (state.isEdit && card != null) {
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        showDropZone()
                        true
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        hideDropZone()
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        } else {
            return false
        }
    }

    private fun showDropZone() {
        dropZone.animate()
            .translationY(0f)
            .setDuration(ANIM_DURATION)
            .setInterpolator(FastOutLinearInInterpolator())
            .start()
    }

    private fun hideDropZone() {
        dropZone.animate()
            .translationY(dropZone.resources.getDimension(R.dimen.dropzone_height_inverse))
            .setDuration(ANIM_DURATION)
            .setInterpolator(FastOutLinearInInterpolator())
            .start()
    }

    private class AddDragListener(
        val actionAdd: View,
        val listener: DropListener
    ) : View.OnDragListener {

        private val selectedColor by lazy { actionAdd.color(R.color.dropzone_green_selected) }
        private val unselectedColor by lazy { actionAdd.color(R.color.dropzone_green) }

        override fun onDrag(v: View, event: DragEvent): Boolean {
            val state = event.localState as? PokemonCardView.DragState
            if (state != null) {
                val card = state.view.card
                if (state.isEdit && card != null) {
                    return when (event.action) {
                        DragEvent.ACTION_DRAG_ENTERED -> {
                            v.setBackgroundColor(selectedColor)
                            true
                        }
                        DragEvent.ACTION_DRAG_EXITED -> {
                            v.setBackgroundColor(unselectedColor)
                            true
                        }
                        DragEvent.ACTION_DROP -> {
                            listener.onAddCard(card)
                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED -> {
                            v.setBackgroundColor(unselectedColor)
                            true
                        }
                        else -> true
                    }
                }
            }
            return false
        }

        companion object {

            fun attach(target: View, dropListener: DropListener): AddDragListener {
                val listener = AddDragListener(target, dropListener)
                target.setOnDragListener(listener)
                return listener
            }
        }
    }

    private class RemoveDragListener(
        val actionRemove: View,
        val listener: DropListener
    ) : View.OnDragListener {

        private val selectedColor by lazy { actionRemove.color(R.color.dropzone_red_selected) }
        private val unselectedColor by lazy { actionRemove.color(R.color.dropzone_red) }

        override fun onDrag(v: View, event: DragEvent): Boolean {
            val state = event.localState as? PokemonCardView.DragState
            if (state != null) {
                val card = state.view.card
                if (state.isEdit && card != null) {
                    return when (event.action) {
                        DragEvent.ACTION_DRAG_ENTERED -> {
                            v.setBackgroundColor(selectedColor)
                            true
                        }
                        DragEvent.ACTION_DRAG_EXITED -> {
                            v.setBackgroundColor(unselectedColor)
                            true
                        }
                        DragEvent.ACTION_DROP -> {
                            listener.onRemoveCard(card)
                            true
                        }
                        DragEvent.ACTION_DRAG_ENDED -> {
                            v.setBackgroundColor(unselectedColor)
                            true
                        }
                        else -> true
                    }
                }
            }
            return false
        }

        companion object {

            fun attach(target: View, dropListener: DropListener): RemoveDragListener {
                val listener = RemoveDragListener(target, dropListener)
                target.setOnDragListener(listener)
                return listener
            }
        }
    }

    interface DropListener {

        fun onAddCard(card: PokemonCard)
        fun onRemoveCard(card: PokemonCard)
    }

    companion object {
        private const val ANIM_DURATION = 150L

        /**
         * Attach a new [EditDragListener] to the target view to handle Drag n Drop
         * operation for editing cards in the deck building interface
         */
        fun attach(target: View, listener: DropListener): EditDragListener {
            val dragListener = EditDragListener(target, listener)
            target.setOnDragListener(dragListener)
            return dragListener
        }
    }
}
