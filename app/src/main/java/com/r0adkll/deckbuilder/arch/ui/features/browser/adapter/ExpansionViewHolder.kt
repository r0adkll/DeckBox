package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.util.bindView


class ExpansionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val logo: ImageView by bindView(R.id.logo)


    fun bind(expansion: Expansion) {
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