package com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.util.extensions.loadOfflineUri
import com.r0adkll.deckbuilder.util.extensions.readableFileSize

class ExpansionCacheViewHolder(
    itemView: View,
    private val deleteClicks: Relay<Expansion>
) : RecyclerView.ViewHolder(itemView) {

    private val icon = itemView.findViewById<ImageView>(R.id.icon)
    private val name = itemView.findViewById<TextView>(R.id.name)
    private val size = itemView.findViewById<TextView>(R.id.size)
    private val actionDelete = itemView.findViewById<Button>(R.id.actionDelete)

    fun bind(item: ExpansionCache) {
        GlideApp.with(itemView)
            .loadOfflineUri(itemView.context, item.expansion.symbolUrl)
            .into(icon)

        name.text = item.expansion.name
        size.text = item.cacheStatus.totalSizeInBytes.readableFileSize()

        actionDelete.setOnClickListener {
            deleteClicks.accept(item.expansion)
        }
    }
}
