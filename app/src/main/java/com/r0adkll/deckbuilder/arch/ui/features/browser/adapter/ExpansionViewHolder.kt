package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ftinc.kit.kotlin.extensions.string
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.util.bindView


class ExpansionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val logo: ImageView by bindView(R.id.logo)
    private val name: TextView by bindView(R.id.name)
    private val series: TextView by bindView(R.id.series)
    private val date: TextView by bindView(R.id.date)


    fun bind(expansion: Expansion) {
        name.text = expansion.name
        series.text = expansion.series
        date.text = string(R.string.expansion_released_date_format, expansion.releaseDate)

        GlideApp.with(itemView)
                .load(expansion.logoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(logo)
    }


    companion object {

        fun create(inflater: LayoutInflater, parent: ViewGroup?): ExpansionViewHolder {
            return ExpansionViewHolder(inflater.inflate(R.layout.item_expansion, parent, false))
        }
    }
}