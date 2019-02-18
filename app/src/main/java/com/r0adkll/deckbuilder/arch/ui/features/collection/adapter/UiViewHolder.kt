package com.r0adkll.deckbuilder.arch.ui.features.collection.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.ProgressLinearLayout
import com.r0adkll.deckbuilder.util.bindView
import com.r0adkll.deckbuilder.util.extensions.max
import kotlin.math.roundToInt


sealed class UiViewHolder<I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)

    class ExpansionSeriesViewHolder(itemView: View) : UiViewHolder<Item.ExpansionSeries>(itemView) {

        private val title by bindView<TextView>(R.id.title)
        private val completion by bindView<TextView>(R.id.completion)


        override fun bind(item: Item.ExpansionSeries) {
            title.text = item.series
            completion.text = itemView.context.getString(R.string.completion_format,
                    item.completion.times(100f).roundToInt().coerceIn(0, 100))
        }
    }

    class ExpansionSetViewHolder(itemView: View) : UiViewHolder<Item.ExpansionSet>(itemView) {

        private val logo by bindView<ImageView>(R.id.logo)
        private val completion by bindView<TextView>(R.id.completion)
        private val count by bindView<TextView>(R.id.count)
        private val progress by bindView<ProgressLinearLayout>(R.id.progress)


        override fun bind(item: Item.ExpansionSet) {
            GlideApp.with(itemView)
                    .load(item.expansion.logoUrl)
                    .into(logo)

            val completionProgress = (item.count.toFloat() / item.expansion.totalCards.toFloat())
                    .coerceIn(0f, 1f)

            count.text = itemView.context.getString(R.string.completion_count_format, item.count, item.expansion.totalCards.max(item.count))
            completion.text = itemView.context.getString(R.string.completion_format,
                    completionProgress.times(100f).roundToInt())
            progress.progress = completionProgress
        }
    }

    private enum class ViewType(@LayoutRes val layoutId: Int) {
        SERIES(R.layout.item_collection_series),
        SET(R.layout.item_collection_expansion);

        companion object {
            val VALUES by lazy { values() }

            fun of(layoutId: Int): ViewType {
                val match = VALUES.firstOrNull { it.layoutId == layoutId }
                match?.let { return match }

                throw EnumConstantNotPresentException(ViewType::class.java, "could not find view type for $layoutId")
            }
        }
    }


    companion object {

        @Suppress("UNCHECKED_CAST")
        fun create(
                itemView: View,
                layoutId: Int
        ): UiViewHolder<Item> {
            val viewType = ViewType.of(layoutId)
            return when(viewType) {
                ViewType.SERIES -> ExpansionSeriesViewHolder(itemView) as UiViewHolder<Item>
                ViewType.SET -> ExpansionSetViewHolder(itemView) as UiViewHolder<Item>
            }
        }
    }
}