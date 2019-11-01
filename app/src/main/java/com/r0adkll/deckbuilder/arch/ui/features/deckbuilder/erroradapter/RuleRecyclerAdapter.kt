package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.erroradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.r0adkll.deckbuilder.R

class RuleRecyclerAdapter(
    context: Context
) : ListAdapter<Int, RuleRecyclerAdapter.RuleViewHolder>(ITEM_CALLBACK) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuleViewHolder {
        return RuleViewHolder.create(inflater, parent)
    }

    override fun onBindViewHolder(vh: RuleViewHolder, i: Int) {
        vh.bind(getItem(i))
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

    companion object {

        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<Int>() {

            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }
}
