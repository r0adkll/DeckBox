package com.r0adkll.deckbuilder.arch.ui.features.missingcards.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion


class ExpansionSpinnerAdapter(context: Context) : ArrayAdapter<Expansion>(context, R.layout.item_expansion) {

    private val inflater = LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: inflater.inflate(R.layout.item_expansion, parent, false)
        val expansion = getItem(position)

        val icon = view.findViewById<ImageView>(R.id.icon)
        val title = view.findViewById<TextView>(R.id.title)

        title.text = expansion.name
        Glide.with(context)
                .load(expansion.symbolUrl)
                .into(icon)

        return view
    }
}