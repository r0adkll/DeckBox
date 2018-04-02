package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.content.Context
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class ExpansionRecyclerAdapter(context: Context) : ListRecyclerAdapter<Expansion, ExpansionViewHolder>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpansionViewHolder {
        return ExpansionViewHolder.create(inflater, parent)
    }


    override fun onBindViewHolder(vh: ExpansionViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        vh.bind(items[i])
    }


    fun setExpansions(expansions: List<Expansion>) {
        items.clear()
        items.addAll(expansions)
        notifyDataSetChanged()
    }
}