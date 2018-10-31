package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.kotlin.extensions.setVisible
import com.ftinc.kit.kotlin.extensions.string
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.util.bindView


sealed class UiViewHolder<I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)

    class ExpansionViewHolder(
            itemView: View,
            private val downloadClicks: Relay<Expansion>
    ): UiViewHolder<Item.ExpansionSet>(itemView) {

        private val logo by bindView<ImageView>(R.id.logo)
        private val name by bindView<TextView>(R.id.name)
        private val series by bindView<TextView>(R.id.series)
        private val date by bindView<TextView>(R.id.date)
        private val actionDownload by bindView<ImageView>(R.id.actionDownload)
        private val downloadProgress by bindView<FrameLayout>(R.id.downloadLayout)


        override fun bind(item: Item.ExpansionSet) {
            name.text = item.expansion.name
            series.text = item.expansion.series
            date.text = string(R.string.expansion_released_date_format, item.expansion.releaseDate)
            downloadProgress.setVisible(item.offlineStatus == CacheStatus.Downloading)

            GlideApp.with(itemView)
                    .load(item.expansion.logoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(logo)

            actionDownload.setOnClickListener {
                downloadClicks.accept(item.expansion)
            }
        }
    }


    private enum class ViewType(@LayoutRes val layoutId: Int) {
        EXPANSION(R.layout.item_expansion);

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
                layoutId: Int,
                downloadClicks: Relay<Expansion>
        ): UiViewHolder<Item> {
            val viewType = ViewType.of(layoutId)
            return when(viewType) {
                ViewType.EXPANSION -> ExpansionViewHolder(itemView, downloadClicks) as UiViewHolder<Item>
            }
        }
    }
}