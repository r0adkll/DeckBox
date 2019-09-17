package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.kotlin.extensions.setVisible
import com.ftinc.kit.kotlin.extensions.string
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.util.bindView


sealed class UiViewHolder<I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)


    class OutlineViewHolder(
            itemView: View,
            private val dismissClicks: Relay<Unit>,
            private val downloadFormat: Relay<Format>
    ): UiViewHolder<Item.OfflineOutline>(itemView) {

        private val actionDownloadStandard by bindView<Button>(R.id.actionDownloadStandard)
        private val actionDownloadExpanded by bindView<Button>(R.id.actionDownloadExpanded)
        private val actionHide by bindView<Button>(R.id.actionHide)


        override fun bind(item: Item.OfflineOutline) {
            actionHide.setOnClickListener {
                dismissClicks.accept(Unit)
            }

            actionDownloadStandard.setOnClickListener {
                downloadFormat.accept(Format.STANDARD)
            }

            actionDownloadExpanded.setOnClickListener {
                downloadFormat.accept(Format.EXPANDED)
            }
        }
    }


    class ExpansionViewHolder(
            itemView: View,
            private val downloadClicks: Relay<Expansion>
    ): UiViewHolder<Item.ExpansionSet>(itemView) {

        private val logo by bindView<ImageView>(R.id.logo)
        private val name by bindView<TextView>(R.id.name)
        private val series by bindView<TextView>(R.id.series)
        private val date by bindView<TextView>(R.id.date)
        private val actionDownload by bindView<ImageView>(R.id.actionDownload)
        private val downloadProgress by bindView<ProgressBar>(R.id.downloadProgress)


        override fun bind(item: Item.ExpansionSet) {
            name.text = item.expansion.name
            series.text = item.expansion.series
            date.text = string(R.string.expansion_released_date_format, item.expansion.releaseDate)
            downloadProgress.setVisible(item.offlineStatus is CacheStatus.Downloading || item.offlineStatus == CacheStatus.Queued)
            downloadProgress.isIndeterminate = (item.offlineStatus as? CacheStatus.Downloading)?.progress == null
            downloadProgress.progress = when (item.offlineStatus) {
                is CacheStatus.Downloading -> item.offlineStatus.progress?.times(100f)?.toInt() ?: 0
                else -> 0
            }
            actionDownload.setImageResource(when(item.offlineStatus) {
                CacheStatus.Queued -> R.drawable.ic_cloud_queue_24px
                is CacheStatus.Downloading -> R.drawable.cloud_sync
                CacheStatus.Cached -> R.drawable.ic_cloud_done_black_24dp
                else -> R.drawable.cloud_download_outline
            })

            if (logo.getTag(R.id.tag_expansion_logo) != item.expansion.logoUrl) {
                itemView.setTag(R.id.tag_expansion_logo, item.expansion.logoUrl)
                GlideApp.with(itemView)
                        .load(item.expansion.logoUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(logo)
            }

            actionDownload.setOnClickListener {
                if (item.offlineStatus == null || item.offlineStatus == CacheStatus.Empty) {
                    downloadClicks.accept(item.expansion)
                }
            }
        }
    }


    private enum class ViewType(@LayoutRes val layoutId: Int) {
        OUTLINE(R.layout.item_expansion_outline),
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
                downloadClicks: Relay<Expansion>,
                dismissClicks: Relay<Unit>,
                downloadFormat: Relay<Format>
        ): UiViewHolder<Item> {
            val viewType = ViewType.of(layoutId)
            return when(viewType) {
                ViewType.OUTLINE -> OutlineViewHolder(itemView, dismissClicks, downloadFormat) as UiViewHolder<Item>
                ViewType.EXPANSION -> ExpansionViewHolder(itemView, downloadClicks) as UiViewHolder<Item>
            }
        }
    }
}
