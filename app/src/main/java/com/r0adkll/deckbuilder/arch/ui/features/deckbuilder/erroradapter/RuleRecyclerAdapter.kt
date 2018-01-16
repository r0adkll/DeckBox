package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.erroradapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class RuleRecyclerAdapter(context: Context) : ListRecyclerAdapter<Int, RuleRecyclerAdapter.RuleViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleViewHolder {
        return RuleViewHolder.create(inflater, parent)
    }


    override fun onBindViewHolder(vh: RuleViewHolder, i: Int) {
        vh.bind(items[i])
    }


    fun setRuleErrors(errors: List<Int>) {
        items.clear()
        items.addAll(errors)
        notifyDataSetChanged()
    }


    class RuleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val text: TextView = itemView as TextView


        fun bind(item: Int) {
            text.setText(item)
        }


        companion object {

            fun create(inflater: LayoutInflater, parent: ViewGroup): RuleViewHolder {
                return RuleViewHolder(inflater.inflate(R.layout.item_rule_error, parent, false))
            }
        }
    }
}