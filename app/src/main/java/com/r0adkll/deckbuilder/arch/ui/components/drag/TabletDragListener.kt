package com.r0adkll.deckbuilder.arch.ui.components.drag

import android.content.res.ColorStateList
import android.support.v4.view.ViewPager
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.util.UIUtils
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import io.pokemontcg.Pokemon
import io.pokemontcg.model.SuperType


class TabletDragListener(
        val dropZone: View,
        val pager: ViewPager
) : View.OnDragListener {

    private val ANIM_DURATION = 150L
    private val SELECTED_COLOR by lazy { dropZone.color(R.color.secondaryColor) }
    private val UNSELECTED_COLOR by lazy { dropZone.color(R.color.white) }

    private val background: ImageView by lazy { dropZone.findViewById<ImageView>(R.id.dropBackground) }
    private val card: ImageView by lazy { dropZone.findViewById<ImageView>(R.id.dropCard) }
    private val message: TextView by lazy { dropZone.findViewById<TextView>(R.id.dropMessage) }

    private var listener: DropListener? = null
    private var lastPage: Int = 0


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
        message.setTextColor(UNSELECTED_COLOR)
    }


    private fun selectDropZone() {
        background.setImageResource(R.drawable.dr_dropzone_background_selected)
        card.imageTintList = ColorStateList.valueOf(SELECTED_COLOR)
        message.setTextColor(SELECTED_COLOR)
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

        /**
         * Attach a new [TabletDragListener] to the target view to handle the
         * Drag n' Drop operation for tablet target
         */
        fun attach(target: View, pager: ViewPager): TabletDragListener {
            val listener = TabletDragListener(target, pager)
            target.setOnDragListener(listener)
            return listener
        }


        /**
         * Attach a new [TabletDragListener] to the target view to handle the
         * Drag n' Drop operation for tablet target
         */
        fun attach(target: View, pager: ViewPager, dropListener: (PokemonCard) -> Unit): TabletDragListener {
            val listener = TabletDragListener(target, pager)
            listener.setDropListener(dropListener)
            target.setOnDragListener(listener)
            return listener
        }
    }
}